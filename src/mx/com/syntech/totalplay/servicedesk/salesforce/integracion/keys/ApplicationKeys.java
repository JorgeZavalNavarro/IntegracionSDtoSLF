package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys;

/**
 *
 * @author dell
 */
public class ApplicationKeys {
    
    // Nombre del archivo de configuración
    public static final String RUTA_ARCHIVO_CONFIG = "C:\\APIs\\IntegracionSDtoSLF\\Config\\IntegracionSDtoSLFConfigPrincipal.properties";
    
    // Constantes de comands para la base de datos
    // se usa despues del nombre de la tabla en la dentro del form: 
    //        from call_req as ticket WITH (NOLOCK)
    public static final String TABLE_WITH_NOLOCK = "  WITH (NOLOCK) ";
    
    // Llaves o identificadores de los procesos que se van a ejecutar
    public static final int TEST_GENERAL = 100;
    public static final int CREATE_TICKET = 200;
    public static final int ADD_LOG_ACTIVITY = 300;
    public static final int CHANGE_STATUS = 400;
    public static final int TRANSFERENCIA = 500;
    
    
    
    
}
