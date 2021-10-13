package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.test;



/**
 *
 * @author Jorge Zavala Navarro
 */
public class WSAdjuntarDocumentoClient {
/**
    public static void main(String[] params) {
        
        
        System.out.println("Ejecutando en: " + AppPropsBean.getPropsVO().getBddUrlFabricante());

        com.totalplay.syntech.integracion.sdm.soainfra.wssdm.wsclient.USDWebService service = new com.totalplay.syntech.integracion.sdm.soainfra.wssdm.wsclient.USDWebService();
        com.totalplay.syntech.integracion.sdm.soainfra.wssdm.wsclient.USDWebServiceSoap port = service.getUSDWebServiceSoap();

        String archivoAdjuntar = "C:\\SYNTECH\\CAPACITACION\\ITIL 4\\Examen Básico de JAVA.txt";
        String usuario = "servicedesk";
        String password = "DeskService01";
        String persid = "cr:1471026";
        String ktDocumentRepository = "doc_rep:1001";
        int ktDocumentFolder = 1001;
        String retorno = null;
        int sid = 0;
        System.out.println("Adjuntando archivo: " + archivoAdjuntar);
        System.out.println("Login: " + usuario);
        System.out.println("Password: " + password);
        System.out.println("Logeando al servicio para obtener SID");
        sid = port.login(usuario, password);
        if (sid > 0) {
            System.out.println("Sesion ID:" + sid);

            // Crear el documento
            System.out.println("Creando el documento con el sid: " + sid);
            ArrayOfString kdAttributes = new ArrayOfString();
            kdAttributes.getString().add("Info del documento");
            String documento = port.createDocument(sid, kdAttributes);
            System.out.println("Información del documento creado: " + documento);
            String idDocumento = get_attribute_value(documento, "id");
            System.out.println("ID del documento: " + idDocumento);
            int idDocumentiInt = Integer.parseInt(idDocumento);

            System.out.println("Armamos el handler del arcfhivo");
            FileDataSource fds = new FileDataSource(archivoAdjuntar);
            DataHandler dhandler = new DataHandler(fds);
            
            System.out.println("Pasamos el handler al cliente port");
            ((org.apache.axis.client.Stub)port)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME);
            ((org.apache.axis.client.Stub)port).addAttachment(dhandler);
            System.out.println("Intentando enviar el archivo: " + archivoAdjuntar);
            retorno = port.createAttmnt(sid, ktDocumentRepository, ktDocumentFolder, idDocumentiInt, "Prueba de carga desde cliente usando el WS", archivoAdjuntar);
            // sid, ktDocumentRepository, ktDocumentFolder, doc_id, "createAttmnt test file", filename
            System.out.println("Resultado: " + retorno);
        } else {
            System.out.println("No se puede logear al sistema.");
        }
        System.out.println("Cerrar sesión.");
        port.logout(sid);
        System.out.println("Listo, proceso terminado");
    }

    
    private static String get_attribute_value(String result, String attribute_name) {
        String return_val = "";
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(result)));
            Element root = doc.getDocumentElement();
            NodeList udsObjectList = root.getElementsByTagName("UDSObject");
            Element udsObjectElement = (Element) root;

            NodeList attributesList = udsObjectElement.getElementsByTagName("Attributes");
            if (attributesList.getLength() > 0) {
                Element attributesElement = (Element) attributesList.item(0);
                NodeList attributeList = attributesElement.getElementsByTagName("Attribute");

                for (int i = 0; i < attributeList.getLength(); i++) {
                    Element attributeElement = (Element) attributeList.item(i);
                    NodeList attrNameList = attributeElement.getElementsByTagName("AttrName");

                    Element attrNameElement = (Element) attrNameList.item(0);
                    Text attrNameText = (Text) attrNameElement.getFirstChild();
                    String attrNameString = attrNameText.getNodeValue();

                    if (attrNameString.equalsIgnoreCase(attribute_name)) {
                        // Each Attribute element will have one AttrValue, though it may be null
                        NodeList attrValueList = attributeElement.getElementsByTagName("AttrValue");
                        Element attrValueElement = (Element) attrValueList.item(0);
                        Text attrValueText = (Text) attrValueElement.getFirstChild();
                        if (attrValueText != null) {
                            return_val = attrValueText.getNodeValue();
                        }
                        break;
                    }

                }
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred:" + ex.getMessage());
            ex.printStackTrace(System.out);
        }

        return return_val;

    }
**/
}
