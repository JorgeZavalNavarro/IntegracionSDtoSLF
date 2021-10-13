package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.keys.ApplicationKeys;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class AppPropsBean {

    // Propiedades de la clase
    private static AppPropsVO propsVO = null;
    private static Properties props = new Properties();

    static final Category log = Category.getInstance(AppPropsBean.class);

    static {

        log.info("Cargando propiedades de la clase...");

        // Cargar propiedades desde el archivo de configuración
        try {

            // Inicializamos las propiedades correspondientes
            cargarProps();

            // Inicializamos los logs
            iniciarLogs();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void iniciarLogs() throws FileNotFoundException, IOException {
        System.out.println("Iniciar los archivos de los logs (.logs)...");
        Properties logProperties = new Properties();
        logProperties.load(new FileInputStream(AppPropsBean.getPropsVO().getPathConfigLogs()));
        PropertyConfigurator.configure(logProperties);
        log.info("Logs inicializados satisfactoriamente !!");
    }

    private static void cargarProps() throws FileNotFoundException, IOException {
        InputStream is = AppPropsBean.class.getResourceAsStream(ApplicationKeys.ARCHIVO_PROPIEDADES_WEB);
        if (is == null) {
            is = new FileInputStream(ApplicationKeys.ARCHIVO_PROPIEDADES_WEB);
        }
        props.load(is);

        // Inicializar la clase con las propiedades
        propsVO = new AppPropsVO();

        // Propiedades principales de la aplicación y de configuracion
        propsVO.setAmbienteEjecucion(props.getProperty(AppPropsKeys.AMBIENTE_EJECUCION));
        propsVO.setPathConfigLogs(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.PATH_CONFIG_LOGS));
        propsVO.setQueryTimeoutSecs(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.QUERY_TIMEOUT_SECS));

        // propiedades de conexión al servicio web soap de Service Desk
        propsVO.setWssdmWssoapUrl(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSSDM_WSSOAP_URL));
        propsVO.setWssdmWssoapUsuario(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSSDM_WSSOAP_USUARIO));
        propsVO.setWssdmWssoapPassword(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSSDM_WSSOAP_PASSWORD));
        propsVO.setWssdTimeoutConect(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSSD_TIMEOUT_CONECT));
        propsVO.setWssdTimeoutRead(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSSD_TIMEOUT_READ));

        // Propiedades de conexión a la base de datos de service desk
        propsVO.setBddClassDriver(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CLASS_DRIVER));
        propsVO.setBddConexionBasedatos(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CONEXION_BASEDATOS));
        propsVO.setBddConexionPassword(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CONEXION_PASSWORD));
        propsVO.setBddConexionPuerto(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CONEXION_PUERTO));
        propsVO.setBddConexionServidor(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CONEXION_SERVIDOR));
        propsVO.setBddConexionUsuario(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_CONEXION_USUARIO));
        propsVO.setBddUrlFabricante(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.BDD_URL_FABRICANTE));

        // Propiedades de conexión para los web service de TPE
        propsVO.setWstpeCommentUrl(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_COMMENT_URL));
        propsVO.setWstpeCommentIp(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_COMMENT_IP));
        propsVO.setWstpeCommentPassword(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_COMMENT_PASSWORD));
        propsVO.setWstpeCommentUsuario(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_COMMENT_USUARIO));
        
        propsVO.setWstpeUpdateUrl(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_UPDATE_URL));
        propsVO.setWstpeUpdateMswip(props.getProperty(propsVO.getAmbienteEjecucion() + "." + AppPropsKeys.WSTPE_UPDATE_MSWIP));
        

    }

    public static AppPropsVO getPropsVO() {
        return propsVO;
    }

    public static void setPropsVO(AppPropsVO propsVO) {
        AppPropsBean.propsVO = propsVO;
    }

}
