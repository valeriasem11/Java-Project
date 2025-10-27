import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQL {
    public static Statement statement;

    public static void connection(Connection connection)
            throws ClassNotFoundException, SQLException {
        statement = connection.createStatement();
    }

    // Метод создает таблицу
    public static void CreateTable()
            throws ClassNotFoundException, SQLException {
        String table = "CREATE TABLE if not exists ForbesTable (" +
                "'Rank' INT," +
                "'Name' TEXT," +
                "'Networth' REAL, " +
                "'Age' INT, " +
                "'Country' TEXT, " +
                "'Source' TEXT, " +
                "'Industry' TEXT) ";
        statement.execute(table);
    }

    // Метод заполняет таблицу
    public static void WriteTable(ArrayList<Forbes> arr, Connection connection) throws SQLException {
        for (Forbes f : arr) {
            String insert = "INSERT INTO ForbesTable VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pr = connection.prepareStatement(insert);
            pr.setObject(1, f.getRank());
            pr.setObject(2, f.getName());
            pr.setObject(3, f.getNetworth());
            pr.setObject(4, f.getAge());
            pr.setObject(5, f.getCountry());
            pr.setObject(6, f.getSource());
            pr.setObject(7, f.getIndustry());
            pr.execute();
        }
    }

    // Задание 1: Получить список стран (топ-10)
    public static List<String> GetCountries(Connection conn) throws SQLException {
        List<String> countries = new ArrayList<>();
        String query = "SELECT Country FROM 'ForbesTable' GROUP BY Country ORDER BY SUM(Networth) DESC LIMIT 10";
        PreparedStatement pr = conn.prepareStatement(query);
        ResultSet rs = pr.executeQuery();
        while (rs.next()) {
            countries.add(rs.getString(1));
        }
        return countries;
    }

    // Задание 1: Получить общий капитал по странам (топ-10)
    public static List<Double> GetTotalNetworthByCountry(Connection conn) throws SQLException {
        List<Double> networth = new ArrayList<>();
        String query = "SELECT SUM(Networth) AS Total_Networth FROM 'ForbesTable' GROUP BY Country ORDER BY Total_Networth DESC LIMIT 10";
        PreparedStatement pr = conn.prepareStatement(query);
        ResultSet rs = pr.executeQuery();
        while (rs.next()) {
            networth.add(rs.getDouble(1));
        }
        return networth;
    }

    // Задание 2: Самый молодой миллиардер из Франции с капиталом >10 млрд
    public static ResultSet YoungestFrenchBillionaire(Connection conn) throws SQLException {
        String query = "SELECT Name, Age, Networth, Country FROM 'ForbesTable' " +
                "WHERE Country = 'France' AND Networth > 10 " +
                "ORDER BY Age ASC LIMIT 1";
        PreparedStatement pr = conn.prepareStatement(query);
        ResultSet rs = pr.executeQuery();
        return rs;
    }

    // Задание 3: Бизнесмен из США с самым большим капиталом в сфере Energy
    public static ResultSet TopUSEnergyBillionaire(Connection conn) throws SQLException {
        String query = "SELECT Name, Source, Networth, Industry, Country FROM 'ForbesTable' " +
                "WHERE TRIM(Country) = 'United States' AND TRIM(Industry) = 'Energy' " +
                "ORDER BY Networth DESC LIMIT 1";
        PreparedStatement pr = conn.prepareStatement(query);
        ResultSet rs = pr.executeQuery();
        return rs;
    }

    // Дополнительный метод для проверки данных Energy из USA
    public static void CheckEnergyData(Connection conn) throws SQLException {
        String query = "SELECT Name, Country, Industry, Networth FROM 'ForbesTable' " +
                "WHERE TRIM(Industry) = 'Energy' AND TRIM(Country) = 'United States' " +
                "ORDER BY Networth DESC LIMIT 5";
        PreparedStatement pr = conn.prepareStatement(query);
        ResultSet rs = pr.executeQuery();

        System.out.println("Топ-5 бизнесменов из США в Energy:");
        int count = 1;
        while (rs.next()) {
            System.out.printf("%d. %s - %.1f млрд $ (Industry: '%s', Country: '%s')\n",
                    count++,
                    rs.getString("Name"),
                    rs.getDouble("Networth"),
                    rs.getString("Industry"),
                    rs.getString("Country"));
        }
        System.out.println();
    }
}