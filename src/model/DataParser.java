package model;

/*Interfaz para el DataParser*/
public interface DataParser {

	/*Obtener data de un archivo*/
	Object getData(String url,String nameFile);

	/*Guardar data en un archivo*/
	void saveData(String url,String nameFile, Object o);

	/*Obtener numero de archivos de un directorio*/
	int numberFiles(String directory);
}
