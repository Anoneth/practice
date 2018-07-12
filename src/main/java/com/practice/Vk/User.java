package com.practice.Vk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class User {

    private String id;

    private boolean isDeactivated;
    private String deactivatedReason;

    private String firstname;
    private String lastname;
    private String nickname;
    private String photo100;
    private String status;
    private int sex;
    private String bdate;
    private String country;
    private String city;
    private boolean isOnline;
    private String lastseen;

    private String domain;
    private String mPhone;
    private String site;
    private String skype;
    private String instagram;
    private String facebook;
    private String twitter;

    private String occupation;

    private String political;
    private String langs;
    private String religion;
    private String inspiredBy;
    private String peopleMain;
    private String lifeMain;
    private String smoking;
    private String alcohol;

    private String relation;

    private boolean havePersonal;
    private String interests;
    private String music;
    private String activities;
    private String tv;
    private String books;
    private String quotes;
    private String about;
    private String games;
    private String movies;

    private String school;
    private String universities;
    private String career;

    public User(JSONObject jsonObject) {
        String result = "";
        if (jsonObject.containsKey("photo_100")) photo100 = jsonObject.get("photo_100").toString();
        if (jsonObject.containsKey("deactivated")) {
            isDeactivated = true;
            deactivatedReason = jsonObject.get("deactivated").toString();
        } else {
            parseBaseInfo(jsonObject);
            parseOnline(jsonObject);
            parseContacts(jsonObject);
            parseOccupation(jsonObject);
            parsePersonal(jsonObject);
            parseRelation(jsonObject);
            parseInfo(jsonObject);
            parseEducation(jsonObject);
            parseCareer(jsonObject);
        }
    }

    public String getId() {
        return id;
    }
    public String getFullname() {
        return firstname + " " + nickname + " " + lastname;
    }
    public String getBdate() {
        return bdate;
    }
    public String getPhoto100() {
        return photo100;
    }
    public String getStatus() {
        return status;
    }
    public String getSex() {
        if (sex == 0) return "не указан";
        else if (sex == 1) return "женский";
        else return "мужской";
    }
    public String getDeactivatedReason() {
        return deactivatedReason;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public String getAbout() {
        return about;
    }
    public String getActivities() {
        return activities;
    }
    public String getAlcohol() {
        return alcohol;
    }
    public String getBooks() {
        return books;
    }
    public String getCareer() {
        return career;
    }
    public String getDomain() {
        return domain;
    }
    public String getFacebook() {
        return facebook;
    }
    public String getGames() {
        return games;
    }
    public boolean isOnline() {
        return isOnline;
    }
    public String getLastseen() {
        return lastseen;
    }
    public String getInspiredBy() {
        return inspiredBy;
    }
    public String getInstagram() {
        return instagram;
    }
    public String getInterests() {
        return interests;
    }
    public String getLangs() {
        return langs;
    }
    public String getLifeMain() {
        return lifeMain;
    }
    public String getMovies() {
        return movies;
    }
    public String getmPhone() {
        return mPhone;
    }
    public String getMusic() {
        return music;
    }
    public boolean isDeactivated() {
        return isDeactivated;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getPeopleMain() {
        return peopleMain;
    }
    public String getPolitical() {
        return political;
    }
    public String getQuotes() {
        return quotes;
    }
    public String getRelation() {
        return relation;
    }
    public String getReligion() {
        return religion;
    }
    public String getSchool() {
        return school;
    }
    public String getSite() {
        return site;
    }
    public String getSkype() {
        return skype;
    }
    public String getSmoking() {
        return smoking;
    }
    public String getTv() {
        return tv;
    }
    public String getTwitter() {
        return twitter;
    }
    public String getUniversities() {
        return universities;
    }
    public boolean isHavePersonal() {
        return havePersonal;
    }

    private void parseBaseInfo(JSONObject jsonObject) {
        id = jsonObject.get("id").toString();
        firstname = jsonObject.get("first_name").toString();
        lastname = jsonObject.get("last_name").toString();
        if (jsonObject.containsKey("nickname")) nickname = jsonObject.get("nickname").toString();
        if (jsonObject.containsKey("bdate")) bdate = jsonObject.get("bdate").toString();
        else bdate = "не указано";
        if (jsonObject.containsKey("sex")) sex = (int) ((long) jsonObject.get("sex"));
        if (jsonObject.containsKey("country")) {
            JSONObject tmp = (JSONObject) jsonObject.get("country");
            country = tmp.get("title").toString();
        }
        if (jsonObject.containsKey("city")) {
            JSONObject tmp = (JSONObject) jsonObject.get("city");
            city = tmp.get("title").toString();
        }
        if (jsonObject.containsKey("status")) status = jsonObject.get("status").toString();
    }

    private void parseOnline(JSONObject jsonObject) {
        if (jsonObject.containsKey("online")) {
            if ((long) jsonObject.get("online") == 1) isOnline = true;
            else {
                isOnline = false;
                Date date = new Date((long) (((JSONObject) jsonObject.get("last_seen")).get("time")) * 1000);
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yy");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                lastseen = "последний раз " + dateFormat1.format(date) + " в " + dateFormat2.format(date);
            }
        }
    }

    private void parseContacts(JSONObject jsonObject) {
        if (jsonObject.containsKey("mobile_phone")) mPhone = jsonObject.get("mobile_phone").toString();
        if (jsonObject.containsKey("skype")) skype = jsonObject.get("skype").toString();
        if (jsonObject.containsKey("facebook_name"))
            facebook = "id" + jsonObject.get("facebook") + " " + jsonObject.get("facebook_name");
        if (jsonObject.containsKey("twitter")) twitter = jsonObject.get("twitter").toString();
        if (jsonObject.containsKey("instagram")) instagram = jsonObject.get("instagram").toString();
        if (jsonObject.containsKey("site")) site = jsonObject.get("site").toString();
    }

    private void parseOccupation(JSONObject jsonObject) {
        if (jsonObject.containsKey("occupation")) {
            JSONObject t = (JSONObject) jsonObject.get("occupation");
            if (t.get("type").equals("work")) occupation = "работа ";
            else if (t.get("type").equals("school")) occupation = "ученик ";
            else occupation = "студент ";
            occupation += "в " + t.get("name");
        }
    }

    private void parsePersonal(JSONObject jsonObject) {
        if (jsonObject.containsKey("personal")) {
            havePersonal = true;
            JSONObject tmp = (JSONObject) jsonObject.get("personal");
            if (tmp.containsKey("political")) political = parsePolitical((int) ((long) tmp.get("political")));
            if (tmp.containsKey("langs")) langs = parseArray((JSONArray) tmp.get("langs"));
            if (tmp.containsKey("religion")) religion = tmp.get("religion").toString();
            if (tmp.containsKey("inspired_by")) inspiredBy = tmp.get("inspired_by").toString();
            if (tmp.containsKey("people_main")) peopleMain = parsePeopleMain((int) ((long) tmp.get("people_main")));
            if (tmp.containsKey("life_main")) {
                lifeMain = parseLifeMain((int) ((long) tmp.get("life_main")));
                if (tmp.containsKey("smoking")) smoking = parseRelation((int) ((long) tmp.get("smoking")));
                if (tmp.containsKey("alcohol")) alcohol = parseRelation((int) ((long) tmp.get("alcohol")));
            }
        }
        else havePersonal = false;
    }

    private String parsePolitical(int index) {
        switch (index) {
            case 1:
                return "коммунистические";
            case 2:
                return "социалистические";
            case 3:
                return "умеренные";
            case 4:
                return "либеральные";
            case 5:
                return "консервативные";
            case 6:
                return "монархические";
            case 7:
                return "ультраконсервативные";
            case 8:
                return "индифферентные";
            case 9:
                return "либертарианские";
            default:
                return "";
        }
    }

    private String parsePeopleMain(int index) {
        switch (index) {
            case 1:
                return "ум и креативность";
            case 2:
                return "доброта и честность";
            case 3:
                return "красота и здоровье";
            case 4:
                return "власть и богатсво";
            case 5:
                return "смелость и упорство";
            case 6:
                return "юмор и жизнелюбие";
            default:
                return "";
        }
    }

    private String parseArray(JSONArray jsonArray) {
        if (jsonArray.size() > 0) {
            if (jsonArray.size() < 2) return jsonArray.get(0).toString();
            else {
                StringBuffer sb = new StringBuffer();
                sb.append(jsonArray.get(0));
                for (Object lang : jsonArray)
                    sb.append(", ").append(lang.toString());
                return sb.toString();
            }
        }
        return null;
    }
    private String parseLifeMain(int index) {
        switch (index) {
            case 1:
                return "семья и дети";
            case 2:
                return "карьера и деньги";
            case 3:
                return "развлечения и отдых";
            case 4:
                return "наука и исследования";
            case 5:
                return "совершенствование мира";
            case 6:
                return "саморазвитие";
            case 7:
                return "красота и искусство";
            case 8:
                return "слава и влияние";
            default:
                return "";
        }
    }
    private String parseRelation(int index) {
        switch (index) {
            case 1:
                return "резко негативное";
            case 2:
                return "негативное";
            case 3:
                return "компромиссное";
            case 4:
                return "нейтральное";
            case 5:
                return "положительное";
            default:
                return "";
        }
    }
    private void parseInfo(JSONObject jsonObject) {
        if (jsonObject.containsKey("interests")) {
            interests = jsonObject.get("interests").toString();
            music = jsonObject.get("music").toString();
            activities = jsonObject.get("activities").toString();
            movies = jsonObject.get("movies").toString();
            tv = jsonObject.get("tv").toString();
            books = jsonObject.get("books").toString();
            games = jsonObject.get("games").toString();
            about = jsonObject.get("about").toString();
            quotes = jsonObject.get("quotes").toString();
        }
    }
    private void parseRelation(JSONObject jsonObject) {
        if (jsonObject.containsKey("relation"))
            switch ((int) ((long) jsonObject.get("relation"))) {
                case 0:
                    relation = "не указано";
                    break;
                case 1:
                    relation = "не женат/не замужем";
                    break;
                case 2:
                    relation = "есть друг/подруга";
                    break;
                case 3:
                    relation = "помолвлен(-а)";
                    break;
                case 4:
                    relation = "женат/замужем";
                    break;
                case 5:
                    relation = "всё сложно";
                    break;
                case 6:
                    relation = "в активном поиске";
                    break;
                case 7:
                    relation = "влюблён(-а)";
                    break;
                case 8:
                    relation = "в гражданском браке";
        }
    }
    private void parseEducation(JSONObject jsonObject) {
        if (jsonObject.containsKey("schools")) school = parseArray((JSONArray) jsonObject.get("schools"));
        if (jsonObject.containsKey("universities")) universities = parseArray((JSONArray) jsonObject.get("universities"));
    }
    private void parseCareer(JSONObject jsonObject) {
        if (jsonObject.containsKey("career")) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("career");
            StringBuffer sb = new StringBuffer();
            for (Object object : jsonArray) {
                JSONObject t = (JSONObject) object;
                if (t.containsKey("position")) sb.append(t.get("position")).append(" в ");
                if (t.containsKey("group_id")) sb.append("https://vk.com/club").append(t.get("group_id")).append("; ");
                else sb.append(t.get("company")).append("; ");
            }
            career = sb.toString();
        }
    }
}
