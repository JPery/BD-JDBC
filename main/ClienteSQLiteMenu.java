import java.sql.*;
import utils.InputUtils;

public class ClienteSQLiteMenu {
	private final static String DATABASE_PATH = "DGTsimplev2.db";
	private Connection connection = null;
	
  public boolean conectar() {
		try {
		    // Creación de la conexión a la BD
			connection  = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
		    return true;
		}  catch (SQLException e) {
		    // Error. No se ha podido conectar a la BD
			e.printStackTrace();
			return false;
		}
 	}

	public void getVehiculos() {
		// Obtener todos los vehículos con sus marcas y modelos. Mostrar el nombre de la marca, el nombre de la modelo y la matrícula del vehículo
		String sql = "SELECT * FROM vehiculos JOIN modelos USING (cod_marca, cod_modelo) JOIN marcas USING (cod_marca) ORDER BY matricula ASC";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rset = stmt.executeQuery(sql);
			int i = 1;
			while (rset.next()) {
				System.out.println(i + ". " + rset.getString("nom_marca")+ " " +rset.getString("nom_modelo") + ": "+ rset.getString("matricula"));
				i++;
			}
			rset.close();  
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPropietarioVehiculo(String matricula) {
		// Buscar el propietario del vehículo con matrícula coincida con el valor de la variable "matricula". Mostrar el nombre, apellido1, apellido2 y nif de la persona
		String sql = "SELECT * FROM personas p JOIN vehiculos v ON p.nif = v.propietario WHERE matricula = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, matricula);
			ResultSet rset = stmt.executeQuery();
			String propietario = "";
			if (rset.next()){
				propietario = rset.getString("nombre") + " " + rset.getString("Apellido1") + " " + rset.getString("Apellido2") + " - " + rset.getString("nif");
				System.out.println("El propietario del vehículo con matrícula \"" + matricula + "\" es: " + propietario);
			}
			else
				System.out.println("No se encontraron vehiculas con matrícula \"" + matricula+ "\"");
			rset.close(); 
			return propietario;  
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void getPersonasByApellido(String apellido) {
		// Buscar las personas cuyo apellido1 o apellido2 contienen el valor de la variable "apellido". Mostrar el nombre, apellido1, apellido2 y nif de las personas coincidentes
		String sql = "SELECT nif, nombre, Apellido1, Apellido2 FROM personas WHERE Apellido1 LIKE ? OR Apellido2 LIKE ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + apellido + "%");
			stmt.setString(2, "%" + apellido + "%");
			ResultSet rset = stmt.executeQuery();
			if( !rset.next()) {
				System.out.println("No se encontraron personas con el apellido: \"" + apellido +"\"");
			}
			else {
				int i = 1;
				do  {
					System.out.println(i + ". " + rset.getString("nombre") + " " + rset.getString("Apellido1") + " " + rset.getString("Apellido2") + " - " + rset.getString("nif"));
					i++;
				}	while (rset.next());
			}
			rset.close();      
		} catch (SQLException e) {e.printStackTrace();}

	}	

	public void getArticulos(){
		// TODO: Mostrar los distintos artículos de las sanciones ordenados de forma ascentente
		String sql = "";
	}

	public void getSancionesByNif(String nif) {
		// TODO: Buscar las sanciones donde infractor = nif. Mostrar fecha, artículo e importe
		String sql = "";
	}	

	public void getVehiculosByArticuloSancion(String articulo){
		// TODO: Obtener las sanciones cuyo artículo = articulo. Mostrar el expediente de la sanción, la marca, el modelo y la matrícula del vehículo
		String sql = "";
	}

	public void getSancionesByMarca(String marca){
		// TODO: Buscar las sanciones cuya marca de vehículo = marca. Mostrar el expediente de la sanción, el nombre, apellido1, apellido2 y el nif del infractor así como la marca, el modelo y la matrícula del vehículo y la fecha y el importe de la sanción
		String sql = "";
	}

	public void deleteSancion(int expediente){
		// TODO: Ejecutar consulta y mostrar el número de filas afectadas (debería ser 1 si la sanción fue eliminada o 0 si el expediente no existe)
		String sql = "";
	}


	public void insertModelo(String marca, String modelo, int potencia){
		// TODO: Obtener el id de la marca si existe o crearla si no existe. Pista: el nuevo cod_marca debería ser max(cod_marca) + 1 de marcas
		// TODO: Comprobar que no existe el modelo e insertar nuevo modelo con cod_marca, nom_modelo y potencia. Pista: el nuevo cod_modelo debería ser max(cod_modelo) + 1 de modelos
		String getMarcaSQL = "";
		String insertMarcaSQL = "";
		String getModeloSQL = "";
		String insertModeloSQL = "";
		PreparedStatement stmt;
		ResultSet rs;
	}

	public static void main(String[] args) {
		ClienteSQLiteMenu cliente = new ClienteSQLiteMenu();
		// Realizar la conexión a la Base de Datos
		if (!cliente.conectar()) {
			System.out.println("Error: Conexion no realizada.");
			System.exit(-1);
		}
		else
			System.out.println("Conectado a la base de datos.");
		boolean exit = false;
		do 
		{	
			int option = InputUtils.menu();
			switch (option) {
				case 1:
					// Listar  vehículos
					cliente.getVehiculos();
					break;
				case 2: 
					// Buscar propietario de vehículo por matrícula
					String matriculaABuscar = InputUtils.readInput("Introduce una matrícula para buscar");
					cliente.getPropietarioVehiculo(matriculaABuscar);
					break;
				case 3:
					// Buscar persona por apellido
					String apellidoABuscar = InputUtils.readInput("Introduce un apellido para buscar");
					cliente.getPersonasByApellido(apellidoABuscar);
					break;
				case 4:
					// Listar los diferentes artículos de las sanciones
					cliente.getArticulos();
					break;
				case 5:
					// Listar sanciones por nif
					String nifABuscar = InputUtils.readInput("Introduce un nif para buscar");
					cliente.getSancionesByNif(nifABuscar);
					break;
				case 6:
					// Buscar vechículos por artículo de la sanción
					String articulo = InputUtils.readInput("Introduce un artículo para buscar");
					cliente.getVehiculosByArticuloSancion(articulo);
					break;
				case 7:
					// Buscar las sanciones para una marca de vehículo
					String marca = InputUtils.readInput("Introduce una marca para buscar").toUpperCase();
					cliente.getSancionesByMarca(marca);
					break;
				case 8:
					// Eliminar una sanción
					int expediente = InputUtils.readInt("Introduce un expediente para eliminar");
					cliente.deleteSancion(expediente);
					break;
				case 9:
					// Añadir modelo de vehículo
					String nombreMarca = InputUtils.readInput("Introduce el nombre de la marca").toUpperCase();
					String nombreModelo = InputUtils.readInput("Introduce el nombre del modelo");
					int potencia = InputUtils.readInt("Introduce la potencia del modelo", 0, 2000);
					cliente.insertModelo(nombreMarca, nombreModelo, potencia);
					break;
				default: 
					// Salir
					if (option != 0){
						System.out.println("Función no implementada");
					}
					exit = true;
					break;
			}
		}
		while(!exit);
	}
}