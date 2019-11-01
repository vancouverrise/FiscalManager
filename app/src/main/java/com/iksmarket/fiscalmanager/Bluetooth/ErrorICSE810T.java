package com.iksmarket.fiscalmanager.Bluetooth;



public class ErrorICSE810T {

    public static void nullResponse(){
        System.err.println("NULL RESPONSE");
    }

    public static void negativeAmount(double sum) {
        System.err.println("You entered negative amount (" + sum + " UAH.)!");
    }

    public static void status0() {
        System.err.println("(S0) принтер не готов".toUpperCase() + "\nРекомендуется проверить принтер на предмет заклинивания печатающего механизма и плотного\n" +
                "закрытия крышек. Если блокировка не устраняется, то необходимо выполнить сброс принтера путем\n" +
                "его выключения и включения.");
    }

    public static void status1() {
        System.err.println("(S1) ошибка модема".toUpperCase() + "\nОбратитесь в сервисный-центр.");
    }

    public static void status2() {
        System.err.println("(S2) ошибка или переполнение фискальной памяти".toUpperCase() + "\nОбратитесь в сервисный-центр.");
    }

    public static void status3() {
        System.err.println("(S3) неправильная дата или ошибка часов".toUpperCase() + "\nОбратитесь в сервисный-центр.");
    }

    public static void status4() {
        System.err.println("(S4) ошибка индикатора".toUpperCase() + "\nПодключите индекатор.");
    }

    public static void status5() {
        System.err.println("(S5) превышение продолжительности смены".toUpperCase() + "\nСделайте Z - Отчет.");
    }

    public static void status6() {
        System.err.println("(S6) снижение рабочего напряжения питания".toUpperCase() + "\nПроверьте блок питания.");
    }

    public static void status7() {
        System.err.println("(S7) команда не существует или запрещена в данном режиме".toUpperCase() + "\nПроверьте последовательность выполнения команд.");
    }

    public static void result0() {
        System.err.println("(R0) нормальное завершение".toUpperCase());

    }

    public static void result1() {
        System.err.println("(R1) ошибка принтера".toUpperCase());
    }

    public static void result2() {
        System.err.println("(R2) закончилась бумага".toUpperCase());
    }

    public static void result4() {
        System.err.println("(R4) сбой фискальной памяти ".toUpperCase());
    }

    public static void result6() {
        System.err.println("(R6) снижение напряжения питания".toUpperCase());
    }

    public static void result8() {
        System.err.println("(R8) фискальная память переполнена".toUpperCase());
    }

    public static void result10() {
        System.err.println("(R10) не было персонализации ".toUpperCase());
    }

    public static void result16() {
        System.err.println("(R16) команда запрещена в данном режиме".toUpperCase());
    }

    public static void result19() {
        System.err.println("(R19) ошибка программирования логотипа".toUpperCase());
    }

    public static void result20() {
        System.err.println("(R20) неправильная длина строки".toUpperCase());
    }

    public static void result21() {
        System.err.println("(R21) неправильный пароль".toUpperCase());
    }

    public static void result22() {
        System.err.println("(R22) несуществующий номер (пароля, строки)".toUpperCase());
    }

    public static void result23() {
        System.err.println("(R23) налоговая группа не существует или не установлена, налоги не вводились".toUpperCase());
    }

    public static void result24() {
        System.err.println("(R24) тип оплат не существует".toUpperCase());
    }

    public static void result25() {
        System.err.println("(R25) недопустимые коды символов ".toUpperCase());
    }

    public static void result26() {
        System.err.println("(R26) превышение количества налогов".toUpperCase());
    }

    public static void result27() {
        System.err.println("(R27) отрицательная продажа больше суммы предыдущих продаж чека".toUpperCase());
    }

    public static void result28() {
        System.err.println("(R28) ошибка в описании артикула".toUpperCase());
    }

    public static void result30() {
        System.err.println("(R30) ошибка формата даты/времени".toUpperCase());
    }

    public static void result31() {
        System.err.println("(R31) превышение регистраций в чеке".toUpperCase());
    }

    public static void result32() {
        System.err.println("(R32) превышение разрядности вычисленной стоимости".toUpperCase());
    }

    public static void result33() {
        System.err.println("(R33) переполнение регистра дневного оборота".toUpperCase());
    }

    public static void result34() {
        System.err.println("(R34) переполнение регистра оплат".toUpperCase());
    }

    public static void result35() {
        System.err.println("(R35) сумма “выдано” больше, чем в денежном ящике".toUpperCase());
    }

    public static void result36() {
        System.err.println("(R36) дата младше даты последнего z-отчета\n".toUpperCase());
    }

    public static void result37() {
        System.err.println("(R37) открыт чек выплат, продажи запрещены".toUpperCase());
    }

    public static void result38() {
        System.err.println("(R38) открыт чек продаж, выплаты запрещены".toUpperCase());
    }

    public static void result39() {
        System.err.println("(R39) команда запрещена, чек не открыт".toUpperCase());
    }

    public static void result40() {
        System.err.println("(R40) переполнена памят артикулов".toUpperCase());
    }

    public static void result41() {
        System.err.println("(R41) команда запрещена до Z-отчета".toUpperCase());
    }

    public static void result42() {
        System.err.println("(R42) команда запрещена до фискализации".toUpperCase());
    }

    public static void result43() {
        System.err.println("(R43) сдача с этой оплаты запрещена".toUpperCase());
    }

    public static void result44() {
        System.err.println("(R44) команда запрещена, чек открыт".toUpperCase());
    }

    public static void result45() {
        System.err.println("(R45) скидки/наценки запрещены, не было продаж".toUpperCase());
    }

    public static void result46() {
        System.err.println("(R46) команда запрещена после начала оплат".toUpperCase());
    }

    public static void result47() {
        System.err.println("(R47) перевышение продолжительности отправки данных больше 72 часа".toUpperCase());
    }

    public static void result48() {
        System.err.println("(R48) нет ответа от модема".toUpperCase());
    }

    public static void reserv0() {
        System.err.println("(RE0) открыт чек служебного отчета".toUpperCase());
    }

    public static void reserv1() {
        System.err.println("(RE1) состояние аварии (команда завершится после устранения ошибки)".toUpperCase());
    }

    public static void reserv2() {
        System.err.println("(RE2) отсутствие бумаги, если принтер не готов".toUpperCase());
    }

    public static void reserv3(int i) {
        System.err.println(("(RE3) чек: " + ((i == 0) ? "продажи" : "выплаты")).toUpperCase());
    }

    public static void reserv4() {
        System.err.println("(RE4) принтер фискализирован".toUpperCase());
    }

    public static void reserv5() {
        System.err.println("(RE5) смена открыта".toUpperCase());
    }

    public static void reserv6() {
        System.err.println("(RE6) открыт чек".toUpperCase());
    }

    public static void reserv7() {
        System.err.println("(RE7) ЭККР не персонализирован".toUpperCase());

    }
}


