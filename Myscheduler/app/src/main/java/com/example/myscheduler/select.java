package com.example.myscheduler;

public class select {
//    public static void main(String[] args)
//    {
//        //asap,
//        String start_day = "2019-07-12/14:00"; // 7월 11 일 11시~12:00시, 7월 19일 19시~22시
//        String end_day = "2019-07-18/19:00";
//        int during = 3;
//        String[] day = { "2019-07-11/15:00~2019-07-12/17:00","2019-07-11/14:00~2019-07-12/15:00","2019-07-14/15:30~2019-07-14/18:20", "2019-07-13/12:10~2019-07-15/14:50","2019-07-15/18:10~2019-07-18/18:14","2019-07-18/16:40~2019-07-18/19:00"};
//        //
//        int d = getDiffDay(start_day,end_day);
//        System.out.println("day : "+d);
//        int[] count = new int[d];
//        for(int i=0; i<day.length; i++)
//        {
//            int c = getDiffDay2(start_day,day[i]);
//
//            int k = getDiffDay2(day[i].substring(0,10),day[i].substring(17,27));
//            for(int t=0;t<(k+1);t++) {
//                if(c+t <d)
//                {
//                    if(c<0) {}
//                    else count[c+t]++;
//                }
//            }
//        }
////        for(int j=0; j<count.length; j++)
////        {
////            System.out.println(j+" :"+ count[j]);
////        }
//        int chai = cha(count,day);
////        System.out.println(cha(count,day));
//        String add_day = add(start_day,chai);
//
////        System.out.println("=====================");
//        int[] time = ms(start_day,end_day,add_day,day);
//        int e = cha2(time,day);
//        String as = "";
//        if(e<10) {
//            as = ""+add_day +"일 " + e+":00시를 모임 시간으로 추천합니다.";
//        }
//        else
//            as = ""+add_day +"일 " + e+":00시를 모임 시간으로 추천합니다.";
////        System.out.println(as);
//    }

    int[] ms(String start_day,String end_day,String add_day,String[] day)
    {
        //day[1] = "2019-07-11/14:00~2019-07-15/19:00"
        //start_day = "2019-07-12/14:00"
        //end_day = "2019-07-12/19:00"
        //add_day = 2019-07-11
        //test = "2019-07-12/12:00~2019-07-12/18:00;
        int[] time = new int[25];
        int smonth = Integer.parseInt(start_day.substring(5,7));
        int sday = Integer.parseInt(start_day.substring(8,10));
        int emonth = Integer.parseInt(end_day.substring(5,7));
        int eday = Integer.parseInt(end_day.substring(8,10));
        int add_month = Integer.parseInt(add_day.substring(5,7));
        int add_day1 = Integer.parseInt(add_day.substring(8,10));

        if(sday == eday)//7-12/14~7-12/19
        {
            for(int i=Integer.parseInt(start_day.substring(11,13)); i<Integer.parseInt(end_day.substring(11,13));i++)
            {
                time[i] = 1; // 1이 가능한시간, 0은 못씀
            }
        }
        else
        {
            if(add_month == smonth)
            {
                if(add_day1 > sday)
                {
                    if(add_day1 < eday) {//7-11/14~7-15/19
                        for(int i=1; i<25;i++){
                            time[i] = 1; // 1이 가능한시간, 0은 못씀
                        }
                    }
                    else if (add_day1 == eday) { //7-11/14~7-12/19
                        for(int i=1; i<Integer.parseInt(end_day.substring(11,13));i++){
                            time[i] = 1; // 1이 가능한시간, 0은 못씀
                        }
                    }
                }
                else {
                    for(int i=Integer.parseInt(start_day.substring(11,13)); i<25;i++){ //7-12/14~7-13/15
                        time[i] = 1; // 1이 가능한시간, 0은 못씀
                    }
                }
            }
            else if(add_month >smonth && add_day1<eday){//8-1:7-25/14~8/2/19
                for(int i=1; i<25;i++){
                    time[i] = 1; // 1이 가능한시간, 0은 못씀
                }
            }
            else if(add_month>smonth && add_day1==eday) {//8-1:7-25/14~8/1/19
                for(int i=1; i<Integer.parseInt(end_day.substring(11,13));i++){
                    time[i] = 1; // 1이 가능한시간, 0은 못씀
                }
            }
        }
        //--test : 2019-7-11/12~7-11/18---- 2019-07-12/14:00~2019-07-12/19:00---------------------------------------------------------
        for(int j = 0; j<day.length; j++) {
            int d_month = Integer.parseInt(day[j].substring(5,7));
            int d_day = Integer.parseInt(day[j].substring(8,10));
            int de_month = Integer.parseInt(day[j].substring(22,24));
            int de_day = Integer.parseInt(day[j].substring(25,27));
            if(d_month==add_month) {
                if(d_day == add_day1) {
                    if(de_day == add_day1) {
                        for(int i=Integer.parseInt(day[j].substring(11,13)); i<Integer.parseInt(day[j].substring(28,30));i++){
                            if(time[i]==0) {}
                            else time[i] +=1;
                        }
                    }
                    else {
                        for(int i=Integer.parseInt(day[j].substring(11,13)); i<25;i++){
                            if(time[i]==0) {}
                            else time[i] +=1;
                        }
                    }
                }
                else if(d_day <add_day1)
                {
                    if(de_day==add_day1)
                    {
                        for(int i=1; i<Integer.parseInt(day[j].substring(28,30));i++){
                            if(time[i]==0) {}
                            else time[i] +=1;
                        }
                    }
                }
            }
//            for(int i=1; i< 25;i++) {
//                System.out.println(i+"시" +time[i]);
//            }
        }
        return time;
    }
    String add(String start_day, int d)
    {
        int year = Integer.parseInt(start_day.substring(0,4));
        int month = Integer.parseInt(start_day.substring(5,7));
        int day = Integer.parseInt(start_day.substring(8,10));

        int max = f_dayofmonth(year,month);
        int k =day+d;
        if(k > max)
        {
            while(k>max) {
                k = k-max;
                if(month ==12)
                {
                    year += 1;
                    month = 1;
                    day = k;
                }
                else {
                    month++;
                    max = f_dayofmonth(year,month);
                    day = k;
                }

            }
        }
        else {
            day = k;
        }
        String add_day ="";
        if(month <10 && day <10)
        {
            add_day = ""+year+"-0"+month+"-0"+day;
        }
        else if(month<10)
        {
            add_day = ""+year+"-0"+month+"-"+day;
        }
        else if(day < 10)
        {
            add_day = ""+year+"-"+month+"-0"+day;
        }
        System.out.println(add_day);
        return add_day;
    }
    int cha(int[] count,String[] c)
    {
        for(int i=0; i<c.length;i++)
        {
            for(int j=0; j<count.length;j++)
            {
                if(count[j] == i)
                {
                    return j;
                }
            }
        }
        return 0;
    }
    int getDiffDay(String start_day,String end_day){ //날짜 일수 계산

        int sdate_year = Integer.parseInt(start_day.substring(0,4));
        int sdate_month = Integer.parseInt(start_day.substring(5,7));
        int sdate_day = Integer.parseInt(start_day.substring(8,10));
        int edate_year = Integer.parseInt(end_day.substring(0,4));
        int edate_month = Integer.parseInt(end_day.substring(5,7));
        int edate_day = Integer.parseInt(end_day.substring(8,10));
        int year_result = edate_year-sdate_year;
        int total = 0;
        for(int i=sdate_year;i<edate_year+1;i++){
            total += f_leapyear(i);
        }
        total += year_result*365;  //년도 차이 일수 계산
        total += -day_cal(sdate_year, sdate_month, sdate_day)+1;  //시작 날짜 일수 계산
        total += day_cal(edate_year, edate_month, edate_day);   //종료 날짜 일수 계산
        return total;
    }
    int getDiffDay2(String start_day, String end_day){ //날짜 일수 계산

        int sdate_year = Integer.parseInt(start_day.substring(0,4));
        int sdate_month = Integer.parseInt(start_day.substring(5,7));
        int sdate_day = Integer.parseInt(start_day.substring(8,10));
        int edate_year = Integer.parseInt(end_day.substring(0,4));
        int edate_month = Integer.parseInt(end_day.substring(5,7));
        int edate_day = Integer.parseInt(end_day.substring(8,10));
        int year_result = edate_year-sdate_year;
        int total = 0;
        for(int i=sdate_year;i<edate_year+1;i++){
            total += f_leapyear(i);
        }
        total += year_result*365;  //년도 차이 일수 계산
        total += -day_cal(sdate_year, sdate_month, sdate_day);  //시작 날짜 일수 계산
        total += day_cal(edate_year, edate_month, edate_day);   //종료 날짜 일수 계산
        return total;
    }
    int day_cal(int yy, int mm, int dd){ //해당년도만의 일수 계산
        int total = 0;
        for(int i=1;i<mm;i++){
            total += f_dayofmonth(yy,i);
        }
        return total+dd;
    }

    int f_dayofmonth(int yy,int mm){
        switch (mm){
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return 28 + f_leapyear(yy);
        }
        return 0;
    }
    int f_leapyear(int yy)  //윤달 계산
    {
        if (yy%4==0 && yy%100!=0 || yy%400==0)
            return 1;
        else
            return 0;
    }
    int cha2(int[] time, String[] day) {
        for(int t=1; t<day.length;t++)
        {
            for(int k =0; k<time.length; k++) {
                if(time[k] == t)
                {
                    return k;
                }
            }
        }
        return 0;
    }
}
