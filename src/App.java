
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;
import org.apache.commons.lang3.StringUtils;

import org.jpl7.*;

class JPL {

    private static ArrayList<ArrayList<String>> rules;
    private static ArrayList<String> antecedent;
    private static ArrayList<String> consequent;
    private static Query queryConection;

    public static void main(String[] args) {

        rules = new ArrayList<ArrayList<String>>();
        antecedent = new ArrayList<String>();
        consequent = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        int count = 1;
        while (true) {
            System.out.println("(Enter): Para insertar mas reglas");
            System.out.println("(1): Continuar con los hechos");

            System.out.format("Regla(%d): ", count);
            String textChain = scanner.nextLine();
            System.out.println("----------------------------------");

            if (textChain.equals("")) {
                continue;
            }
            if (textChain.equals("1")) {
                break;
            }

            if (isRule(textChain) == 1) {
                cleanRule(textChain);
                count++;
            } else if (isRule(textChain) == -1) {
                System.out.format("%s NO ES UNA REGLA.\n", textChain);
            } else {
                System.out.println("ESTA REGLA NO ES DETERMINISTA.\n");
            }
        }

        for (ArrayList<String> rule : rules) {
            for (int i = 0; i < rule.size(); i++) {
                if (i == 0 && !consequent.contains(rule.get(i))) {
                    consequent.add(rule.get(i));
                    continue;
                }
                if (i > 0 && !antecedent.contains(rule.get(i))) {
                    antecedent.add(rule.get(i));
                }
            }
        }

        System.out.println("Selecciona los hechos separados por coma y sin espacios:");
        System.out.println("----------------------------------");

        for (int i = 0; i < antecedent.size(); i++) {
            System.out.format("Hecho(%d): %s" + "\n", i + 1, antecedent.get(i));
        }

        while (true) {

            System.out.println("\n" + "Hechos: ");
            String facts = scanner.nextLine();

            String notmumber = "";

            for (String fact : facts.split(",")) {
                if (!fact.matches("-?\\d+")) {
                    notmumber = fact;
                }
            }

            if (!notmumber.equals("")) {
                System.out.format("Resiva tu seleccion: %s no es una opcion valida.\n", notmumber);
                continue;
            }

            if (facts.split(",").length <= antecedent.size()) {
                for (String fact : facts.split(",")) {

                    if (Integer.parseInt(fact) > 0 && Integer.parseInt(fact) <= antecedent.size()
                            && StringUtils.countMatches(facts, fact) == 1) {
                        saveKnowloge(facts.split(","));
                        break;
                    } else {
                        System.out.println("Revisa tu seleccion: hechos repetidos o no existente\n");
                        continue;
                    }
                }
            } else {
                System.out.println("Has selecionado mas hechos de los que tienes disponible\n");
                continue;
            }

            break;
        }

        System.out.println("\n----------------------------------");
        System.out.println("Que desea saber ? PD: Numeros separados por coma");
        System.out.println("----------------------------------");

        for (int index = 0; index < consequent.size(); index++) {
            System.out.format("Pregunta(%d): %s ?" + "\n", index + 1, consequent.get(index));
        }

        while (true) {

            System.out.println("\nOpcion: ");
            String quetions = scanner.nextLine();

            String notmumber = "";

            for (String fact : quetions.split(",")) {
                if (!fact.matches("-?\\d+")) {
                    notmumber = fact;
                    break;
                }
            }

            if (!notmumber.equals("")) {
                System.out.format("Resiva tu seleccion: %s no es una opcion valida.\n", notmumber);
                continue;
            }

            if (quetions.split(",").length <= consequent.size()) {
                for (String quetion : quetions.split(",")) {

                    if (Integer.parseInt(quetion) > 0 && Integer.parseInt(quetion) <= consequent.size()
                            && StringUtils.countMatches(quetions, quetion) == 1) {

                        System.out.format("Pregunta(%d): %s ?" + "\n", Integer.parseInt(quetion),
                                consequent.get(Integer.parseInt(quetion) - 1));

                        String consult = consequent.get(Integer.parseInt(quetion) - 1).replace(" ", "_");

                        jplQuery(consult);

                    } else {
                        System.out.println("Revisa tu seleccion: Preguntas repetidas, no existente, o no numerico\n");
                        continue;
                    }
                }
            } else {
                System.out.println("Has selecionado mas hechos de los que tienes disponible\n");
                continue;
            }

            break;
        }

        scanner.close();
        
        File f = new File("kb.pl");
        if (f.delete()) {
            System.out.println("\nThe Knowledge Base was deleted");
        }
    }

    public static int isRule(String textChain) {

        int begin = textChain.toLowerCase().indexOf("entonces");
        int end = begin + "entonces".length() - 1;

        if (begin > 1 && end < textChain.length() - 2) {
            if (textChain.split(" entonces ")[1].indexOf(" o ") != -1) {
                return -2;
            }
            return 1;
        }
        return -1;

    }

    public static void cleanRule(String textChain) {
        String rule = textChain.toLowerCase();
        String[] array = { ".", ",", ";", ":", "-", "si " };
        ArrayList<String> auxAntecedent = new ArrayList<String>();
        ArrayList<String> auxConsequent = new ArrayList<String>();

        for (String ch : array) {
            rule = rule.replace(ch, "");
        }

        String[] auxList = rule.split(" entonces ");

        if (StringUtils.countMatches(auxList[0], " o ") > 0) {
            for (String pred : auxList[0].split(" o ")) {
                auxAntecedent.add(pred);
            }
        }

        if (StringUtils.countMatches(auxList[1], " y ") > 0) {
            for (String pred : auxList[1].split(" y ")) {
                auxConsequent.add(pred);
            }
        }

        if (auxAntecedent.size() == 0) {
            for (String pred : auxList[0].split(" y ")) {
                auxAntecedent.add(pred);
            }
        }

        if (auxConsequent.size() == 0) {
            auxConsequent.add(auxList[1]);
        }

        for (String predI : auxConsequent) {
            rules.add(new ArrayList<String>() {
                {
                    add(predI);
                }
            });
            for (String compPred : auxAntecedent) {
                for (String predJ : compPred.split(" y ")) {
                    rules.get(rules.size() - 1).add(predJ);
                }
            }
        }

    }

    public static void saveKnowloge(String[] facts) {
        File f = new File("kb.pl");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            PrintWriter kbFile = new PrintWriter(new FileWriter(f, true));

            kbFile.append("\n% Predicates:Facts\n");
            for (String index : facts) {
                kbFile.append(antecedent.get(Integer.parseInt(index) - 1).replace(" ", "_") + "." + "\n");
            }

            kbFile.append("\n% Propositions:Rules\n");

            for (ArrayList<String> rule : rules) {
                for (int i = 0; i < rule.size(); i++) {
                    if (i == 0) {
                        kbFile.append(rule.get(i).replace(" ", "_") + ":- ");
                        continue;
                    } else if (i == rule.size() - 1) {
                        kbFile.append(rule.get(i).replace(" ", "_") + "." + "\n");
                    } else {
                        kbFile.append(rule.get(i).replace(" ", "_") + ", ");
                    }
                }
            }

            kbFile.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void jplQuery(String consult) {
        queryConection = new Query("consult('kb.pl')");
        queryConection.hasSolution();

        Query query = new Query(consult);
        try {
            query.hasSolution();
            boolean flag = true;

            Map<String, Term>[] result = query.allSolutions();
            for (int i = 0; i < result.length; i++) {
                if (result[i].size() > 0) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                System.out.println("True");
            } else {
                Map<String, Term>[] res = query.allSolutions();

                for (int i = 0; i < res.length; i++) {
                    if (res[i].size() == 0) {
                        System.out.println("True\n");
                    } else {
                        System.out.println(res[i]);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("False\n");
        }
    }
}
