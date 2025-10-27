public class Forbes {
    int rank;
    String name;
    Double networth;
    int age;
    String country;
    String source;
    String industry;

    public Forbes(int rank, String name, Double networth, int age, String country, String source, String industry) {
        this.rank = rank;
        // Убираем все лишние пробелы и кавычки
        this.name = name.trim().replaceAll("\"", "").replaceAll("\\s+", " ");
        this.networth = networth;
        this.age = age;
        this.country = country.trim().replaceAll("\"", "").replaceAll("\\s+", " ");
        this.source = source.trim().replaceAll("\"", "").replaceAll("\\s+", " ");
        this.industry = industry.trim().replaceAll("\"", "").replaceAll("\\s+", " ");
    }

    @Override
    public String toString() {
        return "Forbes{" +
                "rank=" + rank +
                ", name='" + name + '\'' +
                ", networth=" + networth +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", source='" + source + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public Double getNetworth() {
        return networth;
    }

    public int getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getSource() {
        return source;
    }

    public String getIndustry() {
        return industry;
    }
}