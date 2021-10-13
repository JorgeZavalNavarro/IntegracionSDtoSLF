package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author dell
 */
public class RetornoWSSalesForceInfoVO {

    // Constantes de la clase
    private static final Category log = Category.getInstance(RetornoWSSalesForceInfoVO.class);

    // Propiedades de la clase
    private String result = null;
    private String idResult = null;
    private String resultDescription = null;

    RetornoWSSalesForceInfoVO(String retXml) throws RetornoWSSalesForceException, Exception {

        Category log = Category.getInstance(RetornoWSSalesForceInfoVO.class);

        // Procesamod la cadena que recibimos como parametro
        if (retXml != null && !retXml.isEmpty()) {

            // Obtenemos el Documento
            Document docXml = this.loadXMLFromString(retXml);

            // Aplicar la normalización del documento
            docXml.getDocumentElement().normalize();

            // Obtenemos el Nodo Raiz
            log.info("   ::: Root Element :" + docXml.getDocumentElement().getNodeName());
            log.info("   ::: Documento Raiz: " + docXml.getDocumentElement().getNodeName());

            String result = docXml.getElementsByTagName("result").item(0).getTextContent();
            String idResult = docXml.getElementsByTagName("Idresult").item(0).getTextContent();
            String resultDescription = docXml.getElementsByTagName("resultDescription").item(0).getTextContent();

            log.info("   ::: +- result: " + result);
            log.info("   ::: +- idResult: " + idResult);
            log.info("   ::: +- resultDescription: " + resultDescription);

            // Setear las propiedades correspondientes
            this.setIdResult(idResult);
            this.setResult(result);
            this.setResultDescription(resultDescription);

        } else {
            log.error("No se está recibiendo la información ML a procesar !!");
            throw new RetornoWSSalesForceException("No se recibe XML para procesar !!");
        }
    }

    public Document loadXMLFromString(String xml) throws Exception {
        Document retorno = null;
        log.info("   ::: Parseando la cadena XML:");
        log.info(xml);

        if (xml != null && !xml.isEmpty()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xml));
                retorno = builder.parse(is);

            } catch (Exception ex) {
                log.error("   ::: No se puede parsear el código xml");
                log.error(ex.getMessage(), ex);
                throw new RetornoWSSalesForceException(ex);
            }
        } else {
            log.error("No se está recibiendo la información ML a procesar !!");
            throw new RetornoWSSalesForceException("No se recibe XML para procesar !!");
        }
        return retorno;
    }

    // Métodos getters y setters
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIdResult() {
        return idResult;
    }

    public void setIdResult(String idResult) {
        this.idResult = idResult;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

}
