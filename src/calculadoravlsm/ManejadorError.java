package calculadoravlsm;

public abstract class ManejadorError {
    public static void getError(String mensaje){
       Interfaz.Errores.add(mensaje);
    }
}
