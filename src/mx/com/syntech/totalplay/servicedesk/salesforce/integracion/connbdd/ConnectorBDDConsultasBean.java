package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.connbdd;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.keys.CodeKeys;
import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Category;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class ConnectorBDDConsultasBean {
    
    private static final Category log = Category.getInstance(ConnectorBDDConsultasBean.class);

    public static Connection getConectionServiceDesk() throws ConnectorBDDConsultasException {
        Connection retorno = null;
        log.debug("Intentando conectar a la base de datos !!");
        try {

            // Validamos si la sesiòn no es la misma o la sesiòn es nula creamos 
            // un nuevo conector a la base de datos
            Class.forName(AppPropsBean.getPropsVO().getBddClassDriver());

            String url = "jdbc:" + AppPropsBean.getPropsVO().getBddUrlFabricante()
                    + "://" + AppPropsBean.getPropsVO().getBddConexionServidor()
                    + ":" + AppPropsBean.getPropsVO().getBddConexionPuerto()
                    + ";databaseName=" + AppPropsBean.getPropsVO().getBddConexionBasedatos();
            log.debug("URL:" + url);
            DriverManager.setLoginTimeout(Integer.valueOf(AppPropsBean.getPropsVO().getQueryTimeoutSecs()).intValue());
            retorno = DriverManager.getConnection(url, AppPropsBean.getPropsVO().getBddConexionUsuario(), AppPropsBean.getPropsVO().getBddConexionPassword());
            retorno.setAutoCommit(Boolean.FALSE);

        } catch (Exception ex) {
            String retCode = null;
            if(ex.getMessage().contains("connect timed out")){
                retCode = CodeKeys.CODE_340_DATABASE_SQLTIMEOUT;
            }else{
                retCode = CodeKeys.CODE_220_DATABASE_UNREACHABLE;
            }
            throw new ConnectorBDDConsultasException(retCode, ex);
        } catch (Throwable th) {
            throw new ConnectorBDDConsultasException(CodeKeys.CODE_970_DATABASE_ERROR_NC, th);
        }
        return retorno;

    }
    
    public static void main(String...params){
        try {
            getConectionServiceDesk();
        } catch (ConnectorBDDConsultasException ex) {
            ex.printStackTrace();
        }
    }
}
