package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class InputUtils {
	public static String readInput (String text2show) {
		try {
			System.out.print(text2show + ": ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int readInt (String text2show) {
		int value = -1;
		boolean validValue = false;
		do{
			String readValue = InputUtils.readInput(text2show);
			try{
				value = Integer.parseInt(readValue);
				validValue = true;
			}
			catch(NumberFormatException e){
				System.out.println("Valor no válido");
			}
		}
		while (!validValue);
		return value;
	}


	public static int readInt (String text2show, int minValue, int maxValue) {
		int value = -1;
		boolean validValue = false;
		do{
			value = InputUtils.readInt(text2show);
			if (minValue <= value && maxValue >= value)
				validValue = true;
			else
				System.out.println("El valior debe estar entre " + minValue + " y " + maxValue);
		}
		while (!validValue);
		return value;
	}

	public static int menu(){
		List<String>opciones = Arrays.asList(
			"Listar  vehículos",
			"Buscar propietario de vehículo por matrícula",
			"Buscar persona por apellido",
			"Listar artículos de las sanciones",
			"Listar sanciones de una persona por su nif",
			"Buscar vechículos por artículo de la sanción",
			"Buscar las sanciones para una marca de vehículo",
			"Eliminar una sanción",
			"Añadir modelo de vehículo"
		);
		boolean validOption = false;
		int option = 0;
		do
		{
			System.out.println("-------------------------");
			for (int i = 0; i<opciones.size(); i++){
				System.out.println((i+1) + ". "+ opciones.get(i));
			}
			System.out.println("0. Salir");
			try{
				option = InputUtils.readInt("Introduzca una opción", 0, opciones.size());
				if (option <= opciones.size())
					validOption = true;
				else
					System.out.println("Opción no válida");
			}
			catch(NumberFormatException e){
				System.out.println("Opción no válida");
			}
		}
		while(!validOption);
		return option;
	}

}
