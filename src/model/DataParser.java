package model;

public interface DataParser {

	public  Object getData(String url,String nameFile);
	public  void saveData(String url,String nameFile, Object o);
	public int numberFiles(String directory);
}
