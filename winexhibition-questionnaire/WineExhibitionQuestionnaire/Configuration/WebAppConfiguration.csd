<?xml version="1.0" encoding="utf-8"?>
<configurationSectionModel xmlns:dm0="http://schemas.microsoft.com/VisualStudio/2008/DslTools/Core" dslVersion="1.0.0.0" Id="f76490c1-515a-4efc-9233-dd1f3ffacddc" namespace="WineExhibitionQuestionnaire" xmlSchemaNamespace="WineExhibitionQuestionnaire" xmlns="http://schemas.microsoft.com/dsltools/ConfigurationSectionDesigner">
  <typeDefinitions>
    <externalType name="String" namespace="System" />
    <externalType name="Boolean" namespace="System" />
    <externalType name="Int32" namespace="System" />
    <externalType name="Int64" namespace="System" />
    <externalType name="Single" namespace="System" />
    <externalType name="Double" namespace="System" />
    <externalType name="DateTime" namespace="System" />
    <externalType name="TimeSpan" namespace="System" />
  </typeDefinitions>
  <configurationElements>
    <configurationSection name="WebAppConfiguration" codeGenOptions="Singleton, XmlnsProperty" xmlSectionName="webAppConfiguration">
      <elementProperties>
        <elementProperty name="MailJet" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="mailJet" isReadOnly="false">
          <type>
            <configurationElementMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/MailJetConfiguration" />
          </type>
        </elementProperty>
      </elementProperties>
    </configurationSection>
    <configurationElement name="MailJetConfiguration">
      <attributeProperties>
        <attributeProperty name="FromEmail" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="fromEmail" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
        <attributeProperty name="ApiKey" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="apiKey" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
        <attributeProperty name="ApiSecret" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="apiSecret" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
        <attributeProperty name="TemplateOrderId" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="templateOrderId" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/Int32" />
          </type>
        </attributeProperty>
        <attributeProperty name="FromName" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="fromName" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
        <attributeProperty name="EditUrl" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="editUrl" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
        <attributeProperty name="TemplateSentId" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="templateSentId" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/f76490c1-515a-4efc-9233-dd1f3ffacddc/String" />
          </type>
        </attributeProperty>
      </attributeProperties>
    </configurationElement>
  </configurationElements>
  <propertyValidators>
    <validators />
  </propertyValidators>
</configurationSectionModel>