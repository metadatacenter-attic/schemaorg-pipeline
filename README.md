# Welcome to the schemaorg-pipeline code repository

This is the main code repository for the CEDAR schemaorg-pipeline project. Please check [our wiki](https://github.com/metadatacenter/schemaorg-pipeline/wiki) for detailed information about how to use the library and the mapping languages.

## Quick Summary

### The playground

Please visit our [playground](https://schemaorg.metadatacenter.org/playground/) web-app to learn more about the pipeline.

### Programming with the library

The code to construct a pipeline generally uses the pattern below:
```
String output = Pipeline.create()
      .pipe(...Function1...)
      .pipe(...Function2...)
      .pipe(...Function3...)
      ...
      .run(input);
```
where the `Function` is any static function with an input and output of String, the execution order is from top to bottom (i.e., from `Function1` to `FunctionN`), the length of the pipeline is the number of the `pipe` call and the source data is the input.

### CAML Notations

CAML is a recursive acronym for "CAML is Another Mapping Language"

A data map in CAML is composed of one or more mapping definitions. A _mapping definition_ is written as a key-value pair separated by a colon and at least one space, where the key is the _schema.org_ keyword and the value is either a data path, a data object or a constant value.

* **Data Path**. A _data path_ represents the physical data location situated at the source.
```
name:         /Dataset/Title
description:  /Dataset/Description
keyword:      /Dataset/Keywords/Keyword
```

* **Data Object**. A _data object_ is a group of mapping definitions at the same indentation level.
```
distribution:   /Dataset/Distributions/Distribution
   @type:       'DataDownload'
   contentUrl:  /AccessUrl
   fileFormat:  /Format
   publisher:   /Source
```

* **Constant**. A _constant value_ is any other text that is enclosed by single quotation marks.
```
@type:       'Dataset'
inLanguage:  'EN'
```

* **Array**. An _array_ can be constructed by creating a multiple mapping definitions but with the same key label.
```
identifier:  /Dataset/Identifier
identifier:  /Dataset/SecondaryIdentifier
identifier:  /Dataset/Others/PMID
```

The RDF Mapping Language (RML) can be used as well to create the data map. Please check [some of the constraints](https://github.com/metadatacenter/schemaorg-pipeline/wiki/RML) before applying it.

## License
```
Copyright (c) 2016, The Board of Trustees of Leland Stanford Junior University
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

   1. Redistributions of source code must retain the above copyright notice, this list of
      conditions and the following disclaimer.

   2. Redistributions in binary form must reproduce the above copyright notice, this list
      of conditions and the following disclaimer in the documentation and/or other materials
      provided with the distribution.

THIS SOFTWARE IS PROVIDED BY The Board of Trustees of Leland Stanford Junior University
''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL The Board of Trustees of Leland Stanford Junior University OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those of the
authors and should not be interpreted as representing official policies, either expressed
or implied, of The Board of Trustees of Leland Stanford Junior University.
```
