package part2;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class CSVwritein {
    public static void main(String[] args) throws Exception {
        xxx10();

    }


    public static void write(long startTime, String requestType, long latency, int responseCode) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("output.csv", true));
        String line1[] = {String.valueOf(startTime), requestType, String.valueOf(latency), String.valueOf(responseCode)};
        writer.writeNext(line1);
        writer.flush();

    }

    public static void xxx() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("movies_metadata3.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("movies_metadata4.csv", true));
        HashSet<String> set= new HashSet<String>();
        set.add("Drama");
        set.add("Comedy");
        set.add("Action");
        set.add("Fantasy");
        set.add("Horror");
        set.add("Romance");
        set.add("Western");
        set.add("Thriller");
        set.add("Animation");
        int i = 0;
        String[] data = new String[11];
        Iterator<String[]> iterator = reader.iterator();
        while (iterator.hasNext()) {
            String[] next = iterator.next();
            int length = next.length;
            String genres = next[3];
            for(String s : set){
                if(genres.contains(s)){
                    data[0] = s.toUpperCase();
                    break;
                }
            }
            if(data[0] == null) data[0] = "None";
            data[1] = next[8] == null ? "None" : next[8];
            data[2] = next[9] == null ? "None" : next[9];
            data[3] = next[5] == null ? "None" : next[5];
            data[4] = next[6] == null ? "None" : next[6];
            data[5] = next[10] == null ? "None" : next[10];
            data[6] = next[14] == null ? "None" : next[14];
            data[7] = next[21] == null ? "None" : next[21];
            data[8] = next[22] == null ? "None" : next[22];
            if(i == 0) data[9] = "studio";
            else{
                if(next[12].length() > 2){
                    int index1 = next[12].indexOf(":");
                    int index2 = next[12].indexOf(",");
                    data[9] = next[12].substring(index1 + 3, index2 - 1);

                }
                else{
                    data[9] = "None";
                }


            }
            data[10] = next[16];
            i++;
            writer.writeNext(data);
            writer.flush();


        }



    }

    public static void xxx2() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("credits.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("credits11.csv", true));
        String[] data = new String[4];
        Iterator<String[]> iterator = reader.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            String[] next = iterator.next();


            data[0] = next[2];

            int index = next[1].indexOf("Director'");

            int index2 = next[1].indexOf("profile", index + 20);
            if(index != -1 && index2 != -1) data[1] = next[1].substring(index + 20, index2 -4);
            else data[1] = "None";

            int index3 = next[1].indexOf("Directing");

            if(index3 != -1) data[2] = next[1].substring(index3 + 22, index3 + 23);
            else data[2] = "None";
            int index4 = next[1].indexOf("id",index3);
            int index5 = next[1].indexOf("job",index4);
            if(index4 != -1 && index5!=-1) data[3] = next[1].substring(index4+5,index5-3);
            else data[1] = "-1";
            i++;
            writer.writeNext(data);
            writer.flush();




        }


    }

    public static void xxx3() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("credits.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("credits14.csv", true));
        HashMap<String, String> map = new HashMap<>();

        Iterator<String[]> iterator = reader.iterator();
        int i = 0;
        iterator.next();
        while (iterator.hasNext()) {
            String[] next = iterator.next();
            String s1 = next[0];
//            int index = s1.indexOf("cast_id");
//            while(index != -1){
//                String[] data = new String[5];
//                index = s1.indexOf("cast_id");
//                int index1 = s1.indexOf("name");
//                int index3 = s1.indexOf("gender");
//                if(index != -1) {
//                    data[0] = s1.substring(index + 10, index + 11);
//                }else data[0] = "None";
//                if(index1 != -1) {
//                    data[1] = s1.substring(index + 10, index + 11);
//                }else data[0] = "None";
//
//
//
//
//            }
            if(s1.length() < 20) continue;
            s1 = s1.substring(1, s1.length() - 1);
            String[] strs = s1.split("}");
            for(String s : strs){
//                System.out.println(s);
                int index = s.indexOf("cast_id");
                int index1 = s.indexOf("name");
                int index11 = s.indexOf("order", index1);
                int index3 = s.indexOf("gender");
                int index4 = s.indexOf("character");
                int index44 = s.indexOf("credit_id");
                int index5 = s.indexOf("id",index3);

                String[] data = new String[6];
                data[0] = index != -1 && index4 != -1 ? s.substring(index + 10, index4 - 3):"None";

//                System.out.println(data[0]);
                data[1] = index1 != -1 && index11 != -1 ?  s.substring(index1 + 8, index11 - 4):"None";
//                System.out.println(data[1]);
                data[2] = index3 != -1 ? s.substring(index3 + 9, index3 + 10):"None";
                if(index3 != -1){
                    data[2] =s.substring(index3 + 9, index3 + 10).equals("2") ? "M" : "F";
                }
                else{
                    data[2] = "F";
                }
//                System.out.println(data[2]);
                data[3] = index4 != -1 && index44 != -1 ?  s.substring(index4 + 13, index44 - 4):"None";
//                System.out.println(data[3]);
                data[4] = next[2];

                if(index1 != -1 && index5 != -1 && index5 + 5 < index1 -3){
                    data[5] = index1 != -1 && index5 != -1 ?  s.substring(index5 + 5, index1 - 3):"None";
                }
                else{
                    data[5] = "None";
                }

                writer.writeNext(data);
                writer.flush();

            }




//            writer.writeNext(data);
//            writer.flush();




        }


    }

    public static void xxx7() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("credits14.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("credits15.csv", true));
        HashMap<String, String> map = new HashMap<>();

        Iterator<String[]> iterator = reader.iterator();
        int i = 1;
        iterator.next();
        String[] data = new String[7];
        while (iterator.hasNext()) {
            String[] next = iterator.next();
            for (int j = 0; j < next.length; j++) {
                data[j] = next[j];

            }
            data[6] = String.valueOf(i);
            i++;

                writer.writeNext(data);
      writer.flush();



            }


        }






    public static void xxx5() throws Exception {

        CSVReader reader1 = new CSVReader(new FileReader("movies_metadata6.csv"));
        CSVReader reader2 = new CSVReader(new FileReader("credits11.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter("movies_metadata8.csv", true));
        HashMap<String, String> map = new HashMap<>();


        int i = 0;
        String[] data = new String[14];
        Iterator<String[]> iterator2 = reader2.iterator();
        iterator2.next();
        while (iterator2.hasNext()) {
            String[] next = iterator2.next();
            map.put(next[0], next[3]);


        }

        Iterator<String[]> iterator1 = reader1.iterator();
        iterator1.next();
        while (iterator1.hasNext()) {
            String[] next = iterator1.next();
            for(int j = 0; j < next.length;j++){
                data[j] = next[j];
            }
            if(map.containsKey(next[3])){
                data[13] = map.get(next[3]);
            }
            else{
                data[13] = "-1";
            }


            writer.writeNext(data);
            writer.flush();


        }}


        public static void xxx8() throws Exception {

            CSVReader reader1 = new CSVReader(new FileReader("rating.csv"));

            CSVWriter writer = new CSVWriter(new FileWriter("rating2.csv", true));



            int i = 1;
            String[] data = new String[5];
            Iterator<String[]> iterator2 = reader1.iterator();
            iterator2.next();
            while (iterator2.hasNext()) {
                String[] next = iterator2.next();
                for (int j = 0; j < next.length; j++) {
                    data[j] = next[j];

                }
                data[4] = String.valueOf(i);
                i++;

                writer.writeNext(data);
                writer.flush();

            }







            }


    public static void xxx9() throws Exception {

        CSVReader reader1 = new CSVReader(new FileReader("studio2.csv"));
        CSVReader reader2 = new CSVReader(new FileReader("movies_metadata7.csv"));

        CSVWriter writer = new CSVWriter(new FileWriter("rating333.csv", true));
        HashMap<String, String> map = new HashMap<>();
        Iterator<String[]> iterator1 = reader1.iterator();
        iterator1.next();
        while(iterator1.hasNext()){
            String[] next = iterator1.next();
            map.put(next[1], next[0]);
        }



        int i = 1;
        String[] data = new String[15];
        Iterator<String[]> iterator2 = reader2.iterator();

        while (iterator2.hasNext()) {
            String[] next = iterator2.next();
            for (int j = 0; j < next.length; j++) {
                data[j] = next[j];

            }
            if(map.containsKey(next[9])){
                data[14] = map.get(next[9]);
            }
            else{
                data[14] = "-1";
            }

            writer.writeNext(data);
            writer.flush();

        }







    }

    public static void xxx10() throws Exception {

        CSVReader reader1 = new CSVReader(new FileReader("ratings_small.csv"));


        CSVWriter writer = new CSVWriter(new FileWriter("ratings_small4.csv", true));



        int i = 0;
        String[] data = new String[5];
        Iterator<String[]> iterator2 = reader1.iterator();

        while (iterator2.hasNext()) {
            String[] next = iterator2.next();
            for (int j = 0; j < next.length; j++) {
                data[j] = next[j];

            }
            if(i == 0) data[4] = "ratingid";
            else data[4] = String.valueOf(i);
            i++;

            writer.writeNext(data);
            writer.flush();

        }





    }










        }



