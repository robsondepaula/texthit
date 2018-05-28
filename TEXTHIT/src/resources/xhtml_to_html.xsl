<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                version="1.0">
    
    <xsl:output method="html" indent="yes" version="3.2" />
    
    <xsl:strip-space elements="*" />
    
    <xsl:template match="xhtml:p">
        <xsl:variable name="previous-node-name" select="name(preceding-sibling::*[1])" />
        <xsl:choose>
            <xsl:when test="previous-node-name='table'">
                <xsl:apply-templates />
            </xsl:when>
            <xsl:when test="string-length($previous-node-name)=2 and
            substring($previous-node-name,1,1)='h'">
                <xsl:apply-templates />
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="p">
                    <xsl:apply-templates />
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="xhtml:a">
        <xsl:variable name="next-node-name" select="name(following-sibling::*[1])" />
        <xsl:choose>
            <!-- If this is named and href-less, and the next element is a headline,
         we want to shift that headline inside this -->
            <xsl:when test="not(@href) and @name and string-length($next-node-name)=2
            and substring($next-node-name,1,1)='h'">
                <xsl:element name="a">
                    <xsl:apply-templates select="@*|node()|following-sibling::*[1]">
                        <xsl:with-param name="shifted">yes</xsl:with-param>
                    </xsl:apply-templates>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="a">
                    <xsl:apply-templates select="@*|node()" />
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="xhtml:h1|xhtml:h2|xhtml:h3|xhtml:h4|xhtml:h5|xhtml:h6">
        <xsl:param name="shifted">no</xsl:param>
        <xsl:variable name="previous-node-name" select="name(preceding-sibling::*[1])" />
        <xsl:if test="$shifted='yes' or $previous-node-name!='a'
        or preceding-sibling::*[1]/@href or not(preceding-sibling::*[1]/@name)">
            <xsl:element name="{local-name()}">
                <xsl:apply-templates />
            </xsl:element>
        </xsl:if>
    </xsl:template>  
    
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <!-- go process attributes and children -->
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:attribute name="{local-name()}">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>