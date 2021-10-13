package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.test;

import org.apache.log4j.Category;


/**
 *
 * @author dell
 */
public class TestProcesarArchivoBean {

    private static Category log = Category.getInstance(TestProcesarArchivoBean.class);
/**
    // Propiedades de la clase
    // private static Hashtable<String, String> ht = null;
    public TestProcesarArchivoBean() {
        AppPropsBean.getPropsVO().getBddClassDriver();
    }

    public String enviarAdjunto(String persidTicket) throws EnviarArchivoException, DescomprimireGZException, ConversorBase64Exception, ConsumoSoaInfraAPIRestException {
        String retorno = null;
        Boolean descomprimir = Boolean.FALSE;
        Boolean saltarLogException = Boolean.FALSE;

        log.info("Inicio del proceso...");

        // Variable para el bucle
        String nombreArchivoRepositorio = null;
        String nombreArchivoOriginal = null;
        InputStream is = null;
        DescomprimireGZBean descomprimirGzBean = new DescomprimireGZBean();
        ConversorBase64Bean base64Bean = new ConversorBase64Bean();
        String cadBase64 = null;
        String ContentType = null;
        ConsumoSoaInfraAPIRestClient client = null;
        String retCallWS = null;
        descomprimir = Boolean.TRUE;

        // El contenido del archivo se encuentra en el repositorio
        log.info("        ::: Archivo adjunto existente en el repopsitorio.");

        // Determinamos el nombre del archivo a leer desde el repopsitorio
        nombreArchivoRepositorio = "C:\\SYNTECH\\SERVICIOS\\TotalPlay\\Desarrollos\\15E9CD9625F2214B98C4D18393167C19_Telnet PAM hacia EEM.png.gz";
        nombreArchivoOriginal = "Telnet PAM hacia EEM.png";
        log.info("        ::: Descomprimir el archivo");
        is = descomprimirGzBean.descomprimirTOInputStream(nombreArchivoRepositorio, "png");

        // Convertimos el contenido a base 64
        log.info("        ::: Obtener el contenido del archivo en base 64");
        cadBase64 = base64Bean.convertirInputStreamTOBase64(is);
        // cadBase64 = base64Bean.convertirInputStreamTOBase64(is);
        log.info("        ::: Longitud: " + cadBase64.length());

        // Obtenemos el tipo de archivo
        // ContentType = ht.getOrDefault(listInfoVO.get(i).getTipoArchivoAdjunto(), ApplicationKeys.TIPO_ARCHIVO_TEXT_HTML);
        ContentType = "png";
        log.info("        ::: " + "Obtenemos el tipo de archivo: " + ContentType);

        // Mandamos llam,ar el ws de totalplay
        log.info("        ::: " + "Invocamos el WS de TPE...");
        client = new ConsumoSoaInfraAPIRestClient();

        String numeroTicketSalesforce = "654654654";
        retCallWS = client.callWS(numeroTicketSalesforce, nombreArchivoRepositorio, cadBase64, ContentType);
        log.info("   :: Resultado obtenido: " + retCallWS);
        log.info("   :: Validar si contiene algun error el web service");

        log.info("   ::: Fin de proceso");

        return retorno;
        
    }

    

    public static void main(String... params) {
        try {
            TestProcesarArchivoBean bean = new TestProcesarArchivoBean();
            bean.enviarAdjunto("---");
            
        } catch (Exception ex) {
            log.error("No se pudo procesar la petición.");
            log.error(ex.getLocalizedMessage());

            // 
        }
    }
    * **/

}
