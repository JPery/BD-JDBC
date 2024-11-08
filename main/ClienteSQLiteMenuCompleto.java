import java.sql.*;
import utils.InputUtils;

public class ClienteSQLiteMenuCompleto {
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
		// TODO: Mostrar los distintos artículos de las sanciones ordenados de forma ascendente
		String sql = "SELECT DISTINCT articulo FROM sanciones ORDER BY articulo asc;";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            int i = 1;
            while (rset.next()) {
                System.out.println(i + ". " + rset.getString("articulo"));
                i++;
            }
            rset.close();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public void getSancionesByNif(String nif) {
		// TODO: Buscar las sanciones donde infractor = nif. Mostrar fecha, artículo e importe
		String sql = "SELECT * from sanciones where infractor = ? order by fecha asc";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, nif);
			ResultSet rset = stmt.executeQuery();
			if( !rset.next()) {
				System.out.println("No se encontraron sanciones para el nif: \"" + nif +"\"");
			}
			else {
				System.out.println("Sanciones para el nif \"" + nif +"\":");
				int i = 1;
				do  {
					System.out.println(i + ". " + rset.getString("fecha") + ": " + rset.getString("articulo") + " - " + rset.getString("importe") + "€");
					i++;
				}	while (rset.next());
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	

	public void getVehiculosByArticuloSancion(String articulo){
		// TODO: Obtener las sanciones cuyo artículo = articulo. Mostrar el expediente de la sanción, la marca, el modelo y la matrícula del vehículo
		String sql = "SELECT * FROM sanciones JOIN vehiculos USING (matricula) join modelos using (cod_modelo, cod_marca) join marcas using (cod_marca) where articulo = ? order by expediente asc";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, articulo);
			ResultSet rset = stmt.executeQuery();
			if( !rset.next()) {
				System.out.println("No se encontraron sanciones con articulo: \"" + articulo +"\"");
			}
			else {
				System.out.println("Sanciones con artículo \"" + articulo +"\":");
				int i = 1;
				do  {
					System.out.println(i + ". Exp. " + rset.getString("expediente") + ": " + rset.getString("nom_marca") + " " + rset.getString("nom_modelo") + ": " + rset.getString("matricula"));
					i++;
				}	while (rset.next());
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getSancionesByMarca(String marca){
		// TODO: Buscar las sanciones cuya marca de vehículo = marca. Mostrar el expediente de la sanción, el nombre, apellido1, apellido2 y el nif del infractor así como la marca, el modelo y la matrícula del vehículo y la fecha y el importe de la sanción
		String sql = "SELECT * FROM sanciones JOIN vehiculos USING (matricula) join modelos using (cod_modelo, cod_marca) join marcas using (cod_marca) join personas on infractor = nif where nom_marca = ? order by expediente asc";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, marca);
			ResultSet rset = stmt.executeQuery();
			if( !rset.next()) {
				System.out.println("No se encontraron sanciones para vehículos con marca: \"" + marca +"\"");
			}
			else {
				System.out.println("Sanciones para vehículos con marca \"" + marca +"\":");
				int i = 1;
				do  {
					System.out.println(i + ". Exp. " + rset.getString("expediente") + ": " + rset.getString("nombre") + " " + rset.getString("Apellido1") + " " + rset.getString("Apellido2") + " - " + rset.getString("nif") + " - " + rset.getString("nom_marca") + " " + rset.getString("nom_modelo") + " (" + rset.getString("matricula") + "): "+ rset.getString("fecha") + " - " + rset.getString("articulo") + " - " + rset.getString("importe") + "€");
					i++;
				}	while (rset.next());
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteSancion(int expediente){
		String sql = "DELETE FROM sanciones where expediente = ?";
		// TODO: Ejecutar consulta y mostrar el número de filas afectadas (debería ser 1 si la sanción fue eliminada o 0 si el expediente no existe)
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, expediente);
			int deletedRows = stmt.executeUpdate();
			if(deletedRows == 0){
				System.err.println("No se ha encontrado sanción con expediente " + expediente);
			}
			else{
				System.err.println("Se han eliminado " + deletedRows + " sanciones");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void insertModelo(String marca, String modelo, int potencia){
		// TODO: Obtener el id de la marca si existe o crearla si no existe. Pista: el nuevo cod_marca debería ser max(cod_marca) + 1 de marcas
		// TODO: Comprobar que no existe el modelo e insertar nuevo modelo con cod_marca, nom_modelo y potencia. Pista: el nuevo cod_modelo debería ser max(cod_modelo) + 1 de modelos
		String getMarcaSQL = "SELECT * from marcas where nom_marca = ?";
		String insertMarcaSQL = "INSERT INTO marcas(cod_marca, nom_marca) values ((select max(cod_marca) + 1 from marcas), ?)";
		String getModeloSQL = "SELECT * from modelos where nom_modelo = ? AND cod_marca = ?";
		String insertModeloSQL = "INSERT INTO modelos(cod_modelo, cod_marca, nom_modelo, potencia) values ((SELECT IFNULL(max(cod_modelo), 0) + 1 from modelos where cod_marca = ?), ?, ?, ?)";
		PreparedStatement stmt;
		ResultSet rs;
		try {
			int marcaId = -1;
			stmt = connection.prepareStatement(getMarcaSQL);
			stmt.setString(1, marca);
			rs = stmt.executeQuery();
			if(rs.next()){
				marcaId = rs.getInt("cod_marca");
			}
			else{
				stmt = connection.prepareStatement(insertMarcaSQL);
				stmt.setString(1, marca);
				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException("Fallo al crear la marca, no hay filas afectadas");
				}
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					marcaId = generatedKeys.getInt(1);
				}
			}
			stmt = connection.prepareStatement(getModeloSQL);
			stmt.setString(1, modelo);
			stmt.setInt(2, marcaId);
			rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("El modelo \"" + modelo + "\" de la marca \"" + marca + "\" ya existe");
				return;
			}
			stmt = connection.prepareStatement(insertModeloSQL);
			stmt.setInt(1, marcaId);
			stmt.setInt(2, marcaId);
			stmt.setString(3, modelo);
			stmt.setInt(4, potencia);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Fallo al crear el modelo, no hay filas afectadas");
			}
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				int modeloId = generatedKeys.getInt(1);
				System.out.println("El modelo \"" + modelo + "\" de la marca \"" + marca + "\" ha sido creado con id: " + modeloId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ClienteSQLiteMenuCompleto cliente = new ClienteSQLiteMenuCompleto();
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