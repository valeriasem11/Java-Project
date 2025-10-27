import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static ArrayList<Forbes> billionaires;

    public static void main(String[] args) {
        billionaires = new ArrayList<>();

        System.out.println("  АНАЛИЗ ДАННЫХ FORBES - ВАРИАНТ 9");
        System.out.println();

        // Парсинг CSV файла
        System.out.println("Чтение данных из файла Forbes.csv...");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Forbes.csv"))) {
            String str = bufferedReader.readLine(); // Пропускаем заголовок

            while ((str = bufferedReader.readLine()) != null) {
                // Убираем \r и лишние пробелы
                str = str.replaceAll("\\r", "").trim();

                String[] column = str.split(",");

                // Проверка на корректность данных
                if (column.length >= 7) {
                    try {
                        int rank = Integer.parseInt(column[0].trim());
                        String name = column[1].trim();
                        Double networth = Double.parseDouble(column[2].trim());
                        int age = Integer.parseInt(column[3].trim());
                        String country = column[4].trim();
                        String source = column[5].trim();
                        String industry = column[6].trim();

                        Forbes forbes = new Forbes(rank, name, networth, age, country, source, industry);
                        billionaires.add(forbes);
                    } catch (NumberFormatException e) {
                        // Пропускаем строки с ошибками парсинга
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка чтения файла Forbes.csv");
            return;
        }

        System.out.println("✓ Успешно загружено записей: " + billionaires.size());
        System.out.println();

        try {
            Class.forName("org.sqlite.JDBC");

            // Соединяемся с базой данных
            Connection connection = DriverManager.getConnection("jdbc:sqlite:forbes.db");

            if (connection != null) {
                System.out.println("Подключение к базе данных успешно!");

                SQL.connection(connection);
                SQL.CreateTable();
                SQL.WriteTable(billionaires, connection);

                System.out.println("Таблица ForbesTable создана и заполнена!");
                System.out.println();
                System.out.println("ЗАДАНИЕ 1");
                System.out.println("Построение графика общего капитала участников Forbes по странам");
                System.out.println();

                List<String> countries = SQL.GetCountries(connection);
                List<Double> totalNetworth = SQL.GetTotalNetworthByCountry(connection);

                System.out.println("Топ-10 стран по общему капиталу миллиардеров:");
                for (int i = 0; i < countries.size(); i++) {
                    System.out.printf("%2d. %-20s - %8.2f млрд $\n",
                            i + 1, countries.get(i), totalNetworth.get(i));
                }
                System.out.println();

                Chart chart = new Chart("График Forbes - Вариант 9", countries, totalNetworth);
                chart.pack();
                chart.setSize(1200, 800);
                chart.setVisible(true);

                System.out.println("График построен и отображен");
                System.out.println();
                System.out.println();
                System.out.println("ЗАДАНИЕ 2");
                System.out.println("Самый молодой миллиардер из Франции с капиталом > 10 млрд $");
                System.out.println();

                ResultSet youngest = SQL.YoungestFrenchBillionaire(connection);

                if (youngest.next()) {
                    System.out.println("Результат:");
                    System.out.println("  Имя:     " + youngest.getString("Name"));
                    System.out.println("  Возраст: " + youngest.getInt("Age") + " лет");
                    System.out.println("  Капитал: " + youngest.getDouble("Networth") + " млрд $");
                    System.out.println("  Страна:  " + youngest.getString("Country"));
                } else {
                    System.out.println("Не найдено миллиардеров из Франции с капиталом > 10 млрд");
                }
                System.out.println();
                System.out.println();
                System.out.println("ЗАДАНИЕ 3");
                System.out.println("Бизнесмен из США с самым большим капиталом в сфере Energy");
                System.out.println();

                // Сначала проверим данные
                SQL.CheckEnergyData(connection);

                ResultSet topEnergy = SQL.TopUSEnergyBillionaire(connection);

                if (topEnergy.next()) {
                    System.out.println("Результат:");
                    System.out.println("  Имя:      " + topEnergy.getString("Name"));
                    System.out.println("  Компания: " + topEnergy.getString("Source"));
                    System.out.println("  Капитал:  " + topEnergy.getDouble("Networth") + " млрд $");
                    System.out.println("  Индустрия: " + topEnergy.getString("Industry"));
                    System.out.println("  Страна:   " + topEnergy.getString("Country"));
                } else {
                    System.out.println("✗ Не найдено бизнесменов из США в сфере Energy");
                    System.out.println("Проверьте данные в CSV файле и базе данных.");
                }
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("✗ Ошибка при работе с базой данных");
        }
    }
}