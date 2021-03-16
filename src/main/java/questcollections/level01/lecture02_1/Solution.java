package questcollections.level01.lecture02_1;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

import static questcollections.level01.lecture02_1.FileUtils.isExist;
import static questcollections.level01.lecture02_1.FileUtils.renameFile;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) {
        //На вход метода main() подаются два параметра.
        //Первый - path - путь к директории, второй - resultFileAbsolutePath - имя (полный путь) существующего файла, который будет содержать результат.
        String path = args[0];
        String resultFileAbsolutePath = args[1];

        //Создаем каталог и файл из параметров
        File folder = new File(path);
        File resultFile = new File(resultFileAbsolutePath);

        //Переименовать resultFileAbsolutePath в allFilesContent.txt (используй метод FileUtils.renameFile(), и, если понадобится, FileUtils.isExist()).
        File allFilesContentFile = new File(resultFile.getParent() + "/" + "allFilesContent.txt");
        if (isExist(resultFile))
            renameFile(resultFile, allFilesContentFile);

        //writer - поток для записи в переименованный файл.
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allFilesContentFile)))
             //FileOutputStream writer = new FileOutputStream(allFilesContentFile)
        ) {
            //Получением Мапы со списком файлов, мапа сама сортирует
            Map<String, String> sortedMAP = getMapOfFiles(folder, new TreeMap<>());

            for (Map.Entry<String, String> mapData : sortedMAP.entrySet()) {
                System.out.println("Key : " + mapData.getKey() + " Value : " + mapData.getValue());
            }

            //Содержимое всех файлов, размер которых не превышает 50 байт, должно быть записано в файл allFilesContent.txt в отсортированном по имени файла порядке.
            for (Map.Entry<String, String> mapData : sortedMAP.entrySet()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(mapData.getValue())));
                //FileInputStream reader = new FileInputStream(mapData.getValue());
                while (reader.ready()) {
                    //while (reader.available()>0) {
                    writer.write(reader.read());
                }
                reader.close();
                writer.write("\n");
                //writer.write("\n".getBytes());
            }
            //Поток для записи в файл нужно закрыть.
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static double getFileSizeBytes(File file) {
        //Получаем размер вайла в мегабайтах
        return (double) file.length();
    }

    private static Map<String, String> getMapOfFiles(File dir, Map<String, String> map) {
        //Создаем мапу и передаем в неё мапу из параметров
        Map<String, String> sortedMAP = map;
        //Если полученный из параметров файл является файло и <= 50 байт и txt, то добавляем в мапу
        if (dir.isFile() && getFileSizeBytes(dir) <= 50 && getFileExtension(dir).equals("txt")) {
            sortedMAP.put(dir.getName(), dir.getAbsolutePath());
        } else if (dir.isDirectory()) { //Если полученный из параметров файл является каталогом, то получаем мапу со списком фалов
            File[] list = dir.listFiles();
            if (list != null)
                for (File name : list)
                    sortedMAP = getMapOfFiles(name, map);
        }
        return sortedMAP;
    }

    //метод определения расширения файла
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }
}
