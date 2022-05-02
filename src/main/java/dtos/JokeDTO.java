package dtos;

public class JokeDTO {
    private String joke1;
    private String joke1Reference;
    private String joke2;
    private String joke2Reference;

    public JokeDTO(String joke1, String joke2) {
        this.joke1 = joke1;
        this.joke1Reference = "https://api.chucknorris.io/jokes/random";
        this.joke2 = joke2;
        this.joke2Reference = "https://icanhazdadjoke.com";
    }

    public String getJoke1() {
        return joke1;
    }

    public String getJoke1Reference() {
        return joke1Reference;
    }

    public String getJoke2() {
        return joke2;
    }

    public String getJoke2Reference() {
        return joke2Reference;
    }

    @Override
    public String toString() {
        return "JokeDTO{" +
                "joke1='" + joke1 + '\'' +
                ", joke1Reference='" + joke1Reference + '\'' +
                ", joke2='" + joke2 + '\'' +
                ", joke2Reference='" + joke2Reference + '\'' +
                '}';
    }
}
