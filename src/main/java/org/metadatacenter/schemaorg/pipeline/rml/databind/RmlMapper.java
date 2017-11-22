package org.metadatacenter.schemaorg.pipeline.rml.databind;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ArrayNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ConstantNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.MapNodeFactory;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.PairNode;
import org.metadatacenter.schemaorg.pipeline.mapping.translator.ReservedAttributes;
import org.openrdf.model.Namespace;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFFormat;
import com.google.common.base.Charsets;
import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdRMLMappingFactory;
import be.ugent.mmlab.rml.mapdochandler.retrieval.RMLDocRetrieval;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RMLMapping;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.RDFTerm.ObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.PredicateMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.SubjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.TermMap.TermMapType;

public class RmlMapper {

  private final MapNodeFactory mapNodeFactory = new MapNodeFactory();

  public ObjectNode readText(String text) {
    File inputFile = null;
    try {
      inputFile = createInputFile(text);
      ObjectNode root =  mapNodeFactory.rootNode();
      Repository repository = setupRepository(inputFile);
      bindPrefix(repository, root);
      RMLMapping mapping = extractRmlMapping(repository);
      bindRootMap(mapping, root);
      return root;
    } finally {
      if (inputFile != null && inputFile.exists()) {
        inputFile.delete();
      }
    }
  }

  private static File createInputFile(String text) {
    try {
      File tmpFile = File.createTempFile("rml-", ".ttl");
      FileUtils.writeStringToFile(tmpFile, text, Charsets.UTF_8.toString());
      return tmpFile;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Repository setupRepository(File inputFile) {
    RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
    Repository repository = mapDocRetrieval.getMappingDoc(inputFile.getAbsolutePath(), RDFFormat.TURTLE);
    return repository;
  }

  private static RMLMapping extractRmlMapping(Repository repository) {
    StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
    RMLMapping mapping = mappingFactory.extractRMLMapping(repository);
    return mapping;
  }

  private void bindPrefix(Repository repository, ObjectNode root) {
    RepositoryResult<Namespace> nsResult = null;
    try {
      nsResult = repository.getConnection().getNamespaces();
      final ArrayNode prefixes = mapNodeFactory.arrayNode();
      while (nsResult.hasNext()) {
        Namespace ns = nsResult.next();
        String prefixLabel = ns.getPrefix();
        if (!("rr".equals(prefixLabel) || "rml".equals(prefixLabel))) {
          PairNode prefixDefinition = mapNodeFactory.pairNode(prefixLabel, ns.getName());
          prefixes.add(prefixDefinition);
        }
      }
      root.put(ReservedAttributes.PREFIX, prefixes);
    } catch (RepositoryException e) {
      throw new RuntimeException(e);
    } finally {
      if (nsResult != null && !nsResult.isClosed()) {
        try {
          nsResult.close();
        } catch (RepositoryException e) {
          throw new RuntimeException("Failed to close RepositoryResult", e);
        }
      }
    }
  }

  public void bindRootMap(RMLMapping mapping, final ObjectNode root) {
    for (TriplesMap triplesMap : mapping.getTriplesMaps()) {
      if ("root".equals(triplesMap.getShortName().toLowerCase())) {
        bindTriplesMap(triplesMap, root);
      }
    }
  }

  private void bindTriplesMap(TriplesMap triplesMap, final ObjectNode parentNode) {
    SubjectMap sm = triplesMap.getSubjectMap();
    bindSubjectMap(sm, parentNode);
    Set<PredicateObjectMap> predicateObjectMaps = triplesMap.getPredicateObjectMaps();
    for (PredicateObjectMap pom : predicateObjectMaps) {
      if (!pom.hasReferencingObjectMaps()) {
        bindNonReferencingObjectMap(pom, parentNode);
      } else {
        bindReferencingObjectMap(pom, parentNode);
      }
    }
  }

  private void bindSubjectMap(SubjectMap sm, final ObjectNode objectNode) {
    String typeName = getTypeName(sm);
    ConstantNode constantNode = mapNodeFactory.constantNode(typeName);
    objectNode.put(ReservedAttributes.TYPE, constantNode);
  }

  private void bindNonReferencingObjectMap(PredicateObjectMap pom, final ObjectNode objectNode) {
    String attrName = getAttributeName(pom);
    Set<ObjectMap> objectMaps = pom.getObjectMaps();
    if (!objectMaps.isEmpty()) {
      if (objectMaps.size() > 1) {
        ArrayNode arrayNode = mapNodeFactory.arrayNode();
        bindMultipleObjectMaps(objectMaps, arrayNode);
        objectNode.put(attrName, arrayNode);
      } else {
        ObjectMap om = objectMaps.stream().findFirst().get();
        if (om.getTermMapType().equals(TermMapType.CONSTANT_VALUED) ) {
          objectNode.put(attrName, mapNodeFactory.constantNode(getConstantValue(om)));
        } else if (om.getTermMapType().equals(TermMapType.REFERENCE_VALUED)) {
          objectNode.put(attrName, mapNodeFactory.pathNode(getParentPath(om), getDataPath(om)));
        }
      }
    }
  }

  private void bindMultipleObjectMaps(Set<ObjectMap> objectMaps, final ArrayNode arrayNode) {
    for (ObjectMap om : objectMaps) {
      if (om.getTermMapType().equals(TermMapType.CONSTANT_VALUED) ) {
        arrayNode.add(mapNodeFactory.constantNode(getConstantValue(om)));
      } else if (om.getTermMapType().equals(TermMapType.REFERENCE_VALUED)) {
        arrayNode.add(mapNodeFactory.pathNode(getParentPath(om), getDataPath(om)));
      }
    }
  }

  private void bindReferencingObjectMap(PredicateObjectMap pom, final ObjectNode parentNode) {
    String attrName = getAttributeName(pom);
    Set<ReferencingObjectMap> referencingObjectMaps = pom.getReferencingObjectMaps();
    if (!referencingObjectMaps.isEmpty()) {
      if (referencingObjectMaps.size() > 1) {
        ArrayNode arrayNode = mapNodeFactory.arrayNode();
        bindMultipleReferencingObjectMaps(referencingObjectMaps, arrayNode);
        parentNode.put(attrName, arrayNode);
      } else {
        ReferencingObjectMap rom = referencingObjectMaps.stream().findFirst().get();
        TriplesMap triplesMap = rom.getParentTriplesMap();
        ObjectNode objectNode = mapNodeFactory.objectNode(getReferencingDataPath(rom));
        bindTriplesMap(triplesMap, objectNode);
        parentNode.put(attrName, objectNode);
      }
    }
  }

  private void bindMultipleReferencingObjectMaps(Set<ReferencingObjectMap> referencingObjectMaps,
      final ArrayNode arrayNode) {
    for (ReferencingObjectMap rom : referencingObjectMaps) {
      TriplesMap triplesMap = rom.getParentTriplesMap();
      ObjectNode objectNode = mapNodeFactory.objectNode(getReferencingDataPath(rom));
      bindTriplesMap(triplesMap, objectNode);
      arrayNode.add(objectNode);
    }
  }

  private static String getConstantValue(ObjectMap om) {
    return om.getConstantValue().stringValue();
  }

  private static String getParentPath(ObjectMap om) {
    String iterator = om.getOwnTriplesMap().getLogicalSource().getIterator();
    return (iterator == null) ? "" : iterator;
  }

  private static String getDataPath(ObjectMap om) {
    return om.getReferenceMap().getReference();
  }

  private static String getReferencingDataPath(ReferencingObjectMap rom) {
    TriplesMap triplesMap = rom.getParentTriplesMap();
    String iterator = triplesMap.getLogicalSource().getIterator();
    return (iterator == null) ? "" : iterator;
  }

  private String getTypeName(SubjectMap sm) {
    String typeName = sm.getClassIRIs().stream().findFirst().get().stringValue();
    return removeSchemeOrgNamespace(typeName);
  }

  private static String getAttributeName(PredicateObjectMap pom) {
    PredicateMap predicateMap = pom.getPredicateMaps().stream().findFirst().get();
    String attributeName = predicateMap.getConstantValue().stringValue();
    return removeSchemeOrgNamespace(attributeName);
  }

  private static String removeSchemeOrgNamespace(String s) {
    return s.replaceAll("http://schema.org/", "");
  }
}
