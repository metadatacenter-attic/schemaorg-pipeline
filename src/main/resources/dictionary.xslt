<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/*">
    <dictionary>
      <xsl:apply-templates/>
    </dictionary>
  </xsl:template>
  <xsl:template match="miriam/collection[@obsolete='false']">
    <xsl:element name="{concat('ns.', namespace)}">
      <xsl:value-of select="name"/>
    </xsl:element>
    <xsl:element name="{concat('ns.', namespace)}">
      <xsl:value-of select="pattern"/>
    </xsl:element>
    <xsl:element name="{concat('ns.', namespace)}">
      <xsl:value-of select="urischemes/urischeme[@type='URL'][@deprecated='false']"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="text()"/>
</xsl:stylesheet>