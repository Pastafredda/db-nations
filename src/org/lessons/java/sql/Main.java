package org.lessons.java.sql;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inizializzo lo scanner da tastiera
        Scanner scanner = new Scanner(System.in);

        // Parametri di connessione
        String url = "jdbc:mysql://localhost:8889/db-nations";
        String user = "root";
        String password = "root";

        // Provo ad aprire una connection con try with resources
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            System.out.print("Inserisci un parametro di ricerca: ");
            String parametroRicerca = scanner.nextLine();

            String query = "SELECT c.name AS nome, c.country_id AS id, r.name AS nome_della_regione, c2.name AS nome_del_continente " +
                    "FROM countries c " +
                    "JOIN regions r ON r.region_id = c.region_id " +
                    "JOIN continents c2 ON c2.continent_id = r.continent_id " +
                    "WHERE c.name LIKE ?" +
                    "ORDER BY c.name ASC;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + parametroRicerca + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String nomeNazione = resultSet.getString("nome");
                        int idNazione = resultSet.getInt("id");
                        String nomeRegione = resultSet.getString("nome_della_regione");
                        String nomeContinente = resultSet.getString("nome_del_continente");
                        // Stampo la riga
                        System.out.println(idNazione + " - " + nomeNazione + " - " + nomeRegione + " - " + nomeContinente);
                    }
                } catch (SQLException e) {
                    System.out.println("Impossibile eseguire la query.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Impossibile preparare la query.");
                e.printStackTrace();
            }

            System.out.print("Inserisci un id: ");
            String parametroId = scanner.nextLine();

            query = "SELECT l.`language` " +
                    "FROM countries c " +
                    "JOIN country_languages cl ON cl.country_id = c.country_id " +
                    "JOIN languages l ON l.language_id = cl.language_id " +
                    "WHERE c.country_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, parametroId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    String stringaFinale = "Lingue: ";
                    while (resultSet.next()) {
                        String lingua = resultSet.getString("language");
                        stringaFinale += lingua + ", ";
                    }
                    stringaFinale = stringaFinale.substring(0, stringaFinale.length() - 2);
                    System.out.println(stringaFinale);
                } catch (SQLException e) {
                    System.out.println("Impossibile eseguire la query.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Impossibile preparare la query.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Impossibile aprire una connessione.");
            e.printStackTrace();
        }

        scanner.close();


    }
}