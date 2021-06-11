<?xml version="1.0" encoding="utf-8"?>
<configurationSectionModel xmlns:dm0="http://schemas.microsoft.com/VisualStudio/2008/DslTools/Core" dslVersion="1.0.0.0" Id="0476ff75-e2bc-41ae-af23-2ea985e5bb0a" namespace="WineExhibitionQuestionnaire.Configuration" xmlSchemaNamespace="urn:WineExhibitionQuestionnaire.Configuration" xmlns="http://schemas.microsoft.com/dsltools/ConfigurationSectionDesigner">
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
        <elementProperty name="AuthenticationConfig" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="authenticationConfig" isReadOnly="false">
          <type>
            <configurationElementMoniker name="/0476ff75-e2bc-41ae-af23-2ea985e5bb0a/AuthenticationConfiguration" />
          </type>
        </elementProperty>
      </elementProperties>
    </configurationSection>
    <configurationElement name="AuthenticationConfiguration">
      <attributeProperties>
        <attributeProperty name="SecretKey" isRequired="true" isKey="false" isDefaultCollection="false" xmlName="secretKey" isReadOnly="false">
          <type>
            <externalTypeMoniker name="/0476ff75-e2bc-41ae-af23-2ea985e5bb0a/String" />
          </type>
        </attributeProperty>
      </attributeProperties>
    </configurationElement>
  </configurationElements>
  <propertyValidators>
    <validators />
  </propertyValidators>
</configurationSectionModel>