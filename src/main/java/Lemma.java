import java.util.ArrayList;

public class Lemma {
    public String id;
    public WordForm init;
    public ArrayList<WordForm> forms;
    @Override
    public String toString(){
        return "#lemma " + id + " " + init +
                "\n\tforms: [" + forms + "]";
    }
    static class WordForm {
        String str;
        ArrayList<String> grammemes;
        public WordForm(String t){
            this.str = t;
            grammemes = new ArrayList<>();
        }
        public String toString(){
            return "\"" + str + "\": " + grammemes;
        }
    }
}
