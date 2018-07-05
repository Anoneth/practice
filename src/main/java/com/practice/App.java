package com.practice;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String id;
        boolean flag = true;
        do {
            System.out.println("Введите id: ");
            id = reader.readLine();
            try {
                flag = Integer.parseInt(id) <= 0;
                if (flag) System.out.println("id должен быть > 0, попробуйте снова\n");
            }
            catch (Exception ex) {
                System.out.println("Плохое число, попробуйте снова\n");
            }
        }
        while (flag);

        //try {
            String url = "https://api.vk.com/method/users.get?";
            url += "user_ids=" + id;
            url += "&fields=sex,bdate,city,country,home_town,online,domain,has_mobile,contacts,site,education,universities,schools,status,last_seen,followers_count,common_count,occupation,nickname,relatives,relation,personal,connections,exports,wall_comments,activities,interests,music,movies,tv,books,games,about,quotes,timezone,career,military";

            url += "&access_token=&v=5.80";

            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response);

            Object obj2 = new JSONParser().parse(response.toString());
            JSONObject jo = (JSONObject) obj2;
            if (jo.containsKey("error")) {
                System.out.println("Ошибка API\n");
                jo = (JSONObject) jo.get("error");
                System.out.println("Код ошибки: " + jo.get("error_code"));
                System.out.println("Подробности: " + jo.get("error_msg"));
            }
            JSONArray jsonArray = (JSONArray) jo.get("response");
            for (int i = 0; i < jsonArray.size(); i++) {
                System.out.println(userToString((JSONObject)jsonArray.get(i)));
            }
        //} catch (UnknownHostException uhe) {
        //    System.out.println("Ошибка подключения к серверу");
        //} catch (Exception ex) {
        //   System.out.println("Неизвестная ошибка");
        //}
    }

    public static String userToString(JSONObject jsonObject) {
        String result = "";
        if (jsonObject.containsKey("deactivated")) {
            result += "Страница деактивирована, причина: " + jsonObject.get("deactivated") + "\n";
        }
        else {
            result += "Имя: " + jsonObject.get("first_name") + "\n";
            result += "Фамилия: " + jsonObject.get("last_name") + "\n";
            if ((long) jsonObject.get("sex") == 2) result += "Пол: мужской\n";
            else if ((long) jsonObject.get("sex") == 1) result += "Пол: женский\n";
            else result += "Пол не указан\n";
            if (jsonObject.containsKey("bdate"))
                result += "Дата рождения: " + jsonObject.get("bdate") + "\n";
            else result += "Дата рождения не указана\n";
            if (jsonObject.containsKey("country"))
                result += "Страна: " + ((JSONObject) jsonObject.get("country")).get("title") + "\n";
            if (jsonObject.containsKey("city"))
                result += "Город: " + ((JSONObject) jsonObject.get("city")).get("title") + "\n";
            if ((long) jsonObject.get("online") == 1) result += "Онлайн: сейчас\n";
            else {
                Date date = new Date((long) (((JSONObject) jsonObject.get("last_seen")).get("time")) * 1000);
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yy");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                result += "Онлайн: последний раз " + dateFormat1.format(date) + " в " + dateFormat2.format(date) + "\n";
            }
            if (jsonObject.containsKey("skype")) result += "Skype: " + jsonObject.get("skype") + "\n";
            if (jsonObject.containsKey("facebook_name"))
                result += "Facebook: id" + jsonObject.get("facebook") + " " + jsonObject.get("facebook_name") + "\n";
            if (jsonObject.containsKey("twitter")) result += "Twitter: " + jsonObject.get("twitter") + "\n";
            if (jsonObject.containsKey("instagram")) result += "Instagram: " + jsonObject.get("instagram") + "\n";
            result += "Сайт: " + jsonObject.get("site") + "\n";
            result += "Статус: " + jsonObject.get("status") + "\n";
            result += "Количество подписчиков: " + jsonObject.get("followers_count") + "\n";
            if (jsonObject.containsKey("occupation")) {
                JSONObject t = (JSONObject) jsonObject.get("occupation");
                result += "Текущая занятость: ";
                if (((String) t.get("type")).equals("work")) result += "работа ";
                else if (((String) t.get("type")).equals("school")) result += "ученик ";
                else result += "студент ";
                result += "в " + t.get("name") + "\n";
            }
            if (jsonObject.containsKey("personal")) {
                JSONObject tmp = (JSONObject) jsonObject.get("personal");
                if (tmp.containsKey("political")) {
                    result += "Политические предпочтения: ";
                    switch ((int) ((long) tmp.get("political"))) {
                        case 1:
                            result += "коммунистические";
                            break;
                        case 2:
                            result += "социалистические";
                            break;
                        case 3:
                            result += "умеренные";
                            break;
                        case 4:
                            result += "либеральные";
                            break;
                        case 5:
                            result += "консервативные";
                            break;
                        case 6:
                            result += "монархические";
                            break;
                        case 7:
                            result += "ультраконсервативные";
                            break;
                        case 8:
                            result += "индифферентные";
                            break;
                        case 9:
                            result += "либертарианские";
                            break;
                        default:
                            result += "быть такого не может";
                    }
                    result += "\n";
                }
                if (tmp.containsKey("langs")) {
                    JSONArray t = (JSONArray) tmp.get("langs");
                    result += "Языки: ";
                    for (int i = 0; i < t.size(); i++)
                        result += t.get(i) + " ";
                    result += "\n";
                }
                if (tmp.containsKey("religion")) result += "Мировоззрение: " + tmp.get("religion") + "\n";
                if (tmp.containsKey("inspired_by")) result += "Источники вдохновения: " + tmp.get("inspired_by") + "\n";
                if (tmp.containsKey("people_main")) {
                    result += "Главное в людях: ";
                    switch ((int) ((long) tmp.get("people_main"))) {
                        case 1:
                            result += "ум и креативность";
                            break;
                        case 2:
                            result += "доброта и честность";
                            break;
                        case 3:
                            result += "красота и здоровье";
                            break;
                        case 4:
                            result += "власть и богатсво";
                            break;
                        case 5:
                            result += "смелость и упорство";
                            break;
                        case 6:
                            result += "юмор и жизнелюбие";
                            break;
                        default:
                            result += "странные дела";
                    }
                    result += "\n";
                }
                if (tmp.containsKey("life_main")) {
                    result += "Главное в людях: ";
                    switch ((int) ((long) tmp.get("life_main"))) {
                        case 1:
                            result += "семья и дети";
                            break;
                        case 2:
                            result += "карьера и деньги";
                            break;
                        case 3:
                            result += "развлечения и отдых";
                            break;
                        case 4:
                            result += "наука и исследования";
                            break;
                        case 5:
                            result += "совершенствование мира";
                            break;
                        case 6:
                            result += "саморазвитие";
                            break;
                        case 7:
                            result += "красота и искусство";
                            break;
                        case 8:
                            result += "слава и влияние";
                            break;
                        default:
                            result += "undefined";
                    }
                    result += "\n";
                    if (tmp.containsKey("smoking")) {
                        result += "Отношение к курению: ";
                        switch ((int) ((long) tmp.get("smoking"))) {
                            case 1:
                                result += "резко негативное";
                                break;
                            case 2:
                                result += "негативное";
                                break;
                            case 3:
                                result += "компромиссное";
                                break;
                            case 4:
                                result += "нейтральное";
                                break;
                            case 5:
                                result += "положительное";
                                break;
                            default:
                                result += "особенное";
                        }
                        result += "\n";
                    }
                    if (tmp.containsKey("alcohol")) {
                        result += "Отношение к алкоголю: ";
                        switch ((int) ((long) tmp.get("alcohol"))) {
                            case 1:
                                result += "резко негативное";
                                break;
                            case 2:
                                result += "негативное";
                                break;
                            case 3:
                                result += "компромиссное";
                                break;
                            case 4:
                                result += "нейтральное";
                                break;
                            case 5:
                                result += "положительное";
                                break;
                            default:
                                result += "особенное";
                                break;
                        }
                        result += "\n";
                    }
                }
            }
            if (jsonObject.containsKey("relation")) {
                result += "Семейное положение: ";
                switch ((int) ((long) jsonObject.get("relation"))) {
                    case 0:
                        result += "не указано";
                        break;
                    case 1:
                        result += "не женат/не замужем";
                        break;
                    case 2:
                        result += "есть друг/подруга";
                        break;
                    case 3:
                        result += "помолвлен(-а)";
                        break;
                    case 4:
                        result += "женат/замужем";
                        break;
                    case 5:
                        result += "всё сложно";
                        break;
                    case 6:
                        result += "в активном поиске";
                        break;
                    case 7:
                        result += "влюблён(-а)";
                        break;
                    case 8:
                        result += "в гражданском браке";
                }
                result += "\n";
            }
            if (jsonObject.containsKey("interests")) {
                result += "Интересы: " + jsonObject.get("interests") + "\n";
                result += "Любимая музыка: " + jsonObject.get("music") + "\n";
                result += "Деятельность: " + jsonObject.get("activities") + "\n";
                result += "Любимые фильмы: " + jsonObject.get("movies") + "\n";
                result += "Любимые телешоу: " + jsonObject.get("tv") + "\n";
                result += "Любимые книги: " + jsonObject.get("books") + "\n";
                result += "Любимые игры: " + jsonObject.get("games") + "\n";
                result += "О себе: " + jsonObject.get("about") + "\n";
                result += "Любимые цитаты: " + jsonObject.get("quotes") + "\n";
            }
            if (jsonObject.containsKey("schools")) {
                JSONArray tmp = (JSONArray)jsonObject.get("schools");
                result += "Школа: ";
                for (int i = 0; i < tmp.size(); i++)
                    result += ((JSONObject)tmp.get(i)).get("name") + "; ";
                result += "\n";
            }
            if (jsonObject.containsKey("universities")) {
                JSONArray tmp = (JSONArray)jsonObject.get("universities");
                result += "Университет: ";
                for (int i = 0; i < tmp.size(); i++)
                    result += ((JSONObject)tmp.get(i)).get("name") + "; ";
                result += "\n";
            }
            if (jsonObject.containsKey("career")) {
                JSONArray tmp = (JSONArray)jsonObject.get("career");
                result += "Работа: ";
                for (int i = 0; i < tmp.size(); i++) {
                    JSONObject t = (JSONObject) tmp.get(i);
                    if (t.containsKey("position")) result += t.get("position") + " в ";
                    if (t.containsKey("group_id")) result += "https://vk.com/club" + t.get("group_id") + "; ";
                    else result += t.get("company") + "; ";
                }
                result += "\n";
            }
        }
        return result;
    }
}
