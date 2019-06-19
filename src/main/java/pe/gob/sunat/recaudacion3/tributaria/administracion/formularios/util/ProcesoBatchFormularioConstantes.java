package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util;

public class ProcesoBatchFormularioConstantes {

	private ProcesoBatchFormularioConstantes(){}

	public static final String COD_MED_PAG_NPS = "51";

    // estados
    public static final String ESTADO_ACTIVO = "1";

    //valores constantes anexas
    public static final String COD_PARAM_CAB_ANEXAS = "1112";
	public static final String MENSAJE_ANEXAS_EXITOSO = "Proceso Anexas Batch Formulario, correcto";
	public static final String MENSAJE_CASILLAS_EXITOSO = "Proceso Casillas Batch Formulario, correcto";
    public static final String MENSAJE_EXITOSO = "Proceso Batch Formulario, correcto";
    public static final String MENSAJE_FALLIDO = "Proceso Batch Formulario, fallido";
	public static final String MENSAJE_ANEXAS_EXISTE = "Proceso Batch Formulario - Anexa existente ";
	public static final String MENSAJE_ANEXAS_NO_EXISTE = "Proceso Batch Formulario - No existe Anexas para CodFor: ";

    public static final String MENSAJE_BANDEJA_EXISTE = "Proceso Batch Formulario - Bandeja existente ";
	public static final String CODIGO_NO_HAY_BANDEJA = "412";
	public static final String MENSAJE_NO_HAY_BANDEJA = "Proceso Batch Formulario - No existe Reprocesos";
	public static final String SUNATPDT_TMP_BARRA = "/";
	public static final String SUNATPDT_TMP = "/SUNATPDT/tmp";
	public static final String ENCRIPTADOS = "/ENCRIPTADOS";
	public static final String DESENCRIPTADOS = "/DESENCRIPTADOS";
    public static final Long BOLETA_NO_VALIDA = new Long("0");
    public static final Long IND_VAL_CERO = new Long("0");
    public static final String SUFIJO_NUM_PED_PRO = "03000000001";
    public static final String V_NUM_PAGO_CERO = "98";
	public static final String V_NUM_PAGO_MAYOR_CERO = "00";
	public static final String V_COD_ESTADO_PDT = "90";
    public static final String V_COD_ETAPA_CERO_UNO = "01";
    public static final String V_COD_ETAPA_CERO_DOS = "02";
    public static final String DEFAULT_URI_SERVICIO = "/v1/recaudacion/tributaria/administracion/t/procesobatchformulario";
    public static final String COD_PROCESO_FORMULARIO_BANDEJA = "0001";
	public static final String COD_PROCESO_FORMULARIO_ANEXAS = "0006";

    public static final Integer IND_VAL_MIN_BYTE = 1026;
    public static final Integer IND_VALIDA_LIBRO_ELECTRONICO = 100;
    public static final String REGISTRO_PROCESO_OK = "Registro Proceso Bandeja Batch Formulario";
    public static final String VAL_PARAM_TABLA_1115 = "1115";
		public static final String VALOR_NO_EXISTE="00000";
    /**********/
    public static final String COD_FORM_0626 = "0626";
    public static final String COD_FORM_0633 = "0633";
    public static final String COD_FORM_0697 = "0697";
    public static final String COD_FORM_0621 = "0621";
		public static final String COD_FORM_0601 = "0601";
		public static final String COD_FOR_BOLETA = "1662";

    /**********/
    public static final Character IND_VAL_ORIGINAL = '0';
    public static final Character IND_VAL_RECTIFICATORIA = '2';
    public static final Character IND_VAL_SUSTITUTORIA = '1';
		public static final Character IND_VAL_PROCESO = '1';
    public static final Integer IND_VAL_HORA_PRES_PAGO_CERO = 5;

    public static final String COD_RESP_EXITOSO = "200"; //codigo de respuesta exitoso
	public static final String COD_RESP_CON_ERROR = "412"; //codigo de respuesta con error
    public static final String FORM_FECHA_DIA_MES_ANIO_DIAG = "dd/MM/yyyy"; //formato de fecha
    public static final String FORM_FECHA_DIA_MES_ANIO_HORA = "HH:mm:ss"; //formato de fecha


    // Field descriptor #19 Ljava/lang/String;
    public static final String VARIABLE_STRING_VACIA = "";
    public static final Character FLAG_ELIMINADO_ACTIVO = '0'; // Indicador eliminado activo
    public static final Character IND_PROCESO_BANDEJA_EXITOSO = '1'; // Indicador de registro exitoso
    public static final Character IND_PROCESO_BANDEJA_ERRONEO = '0'; // Indicador de registro erroneo
    public static final Character IND_PROCESO_BANDEJA_EN_PROCESO = '2';

    public static final Integer PRIMER_INTENTO = 1; // Indicador de registro erroneo
    public static final String COD_USUARIO_SYSTEM = "SYSTEM";
    public static final String COD_TIP_FOR_PDT = "12";
    //adicional
		public static final Integer IND_VAL_PDT_CERO = 0;

    public static final String VALOR_ACT_0 = "0";
    public static final String FEC_POR_DEFAULT = "01/01/0001";
    public static final char IND_VAL_LIB_0 = '0';
    public static final char IND_VAL_LIB_1 = '1';
    public static final char IND_VAL_DIF_1 = '1';
    public static final char IND_VIG_1 = '1';

    public static final String COD_TRIBUTO_010101 = "010101";

    public static final String VALOR_O1 = "01";
    public static final String VALOR_0697 = "0697";
    public static final String VALOR_0633 = "0633";

    public static final String VALOR_06 = "06";
    public static final String VALOR_07 = "07";
    public static final String VALOR_08 = "08";
    public static final String VALOR_09 = "09";
    public static final String VALOR_10 = "10";
    public static final String VALOR_11 = "11";
    public static final String VALOR_12 = "12";

    public static final Character VALOR_0 = '0';
    public static final Character VALOR_1 = '1';

    public static final Character VALOR_CERO = '0';
    public static final Character VALOR_UNO = '1';
    public static final Character VALOR_DOS = '2';

	public static final String VALOR_TXT2 = "TXT2";
	public static final String VALOR_TXT = "TXT";
    public static final String VALOR_CON = "CON";
    public static final String VALOR_CAB = "CAB";
    public static final String VALOR_DET = "DET";
    public static final String VALOR_ADI = "ADI";
    public static final String VALOR_ADI2 = "ADI2";
	public static final String COD_PRO_BAN_CASILLAS = "0005";
    public static final String COD_PRO_BAN_ANEXAS = "0006";
    public static final Integer CASILLA_16=16;
    public static final Integer CASILLA_17=17;

    public static final String MENSAJE_FORMULARIO_NO_EXISTE = "No hay Formularios Guardados";


}
