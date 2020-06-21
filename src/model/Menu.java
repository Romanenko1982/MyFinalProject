package model;

public class Menu {

    public void printTheWelcomeMenuView() {
        System.out.println("---------------МЕНЮ ПРИВЕТСТВИЯ ПОЛЬЗОВАТЕЛЯ----------------");
        System.out.println("ВАС приветствует кинотеатр, выберите необходимые действия:");
        System.out.println("------------------------------------------------------------\n");
        System.out.println("1. Регистрация нового пользователя");
        System.out.println("2. Вход пользователя");
        System.out.println("3. Выход");
    }

    public void printTheCommonMenuView() {
        System.out.println("------------------------ГЛАВНОЕ МЕНЮ-------------------------");
        System.out.println("1. Показать список фильмов");
        System.out.println("2. Купить билет");
        System.out.println("3. Просмотреть купленные билеты");
        System.out.println("4. Вернуть билет");
        System.out.println("5. Выход");
    }

    public void printManagerEditorMenu() {
        System.out.println("----------------------МЕНЮ МЕНЕДЖЕРА------------------------");
        System.out.println("1. Показать список фильмов");
        System.out.println("2. Сменить дату просмотра фильма");
        System.out.println("3. Показать список пользователей");
        System.out.println("4. Добавить билет пользователю");
        System.out.println("5. Удалить билет пользователя");
        System.out.println("6. Показать купленные билеты пользователя");
        System.out.println("7. Выход");
    }

    public void printTheAdministratorMenuView() {
        System.out.println("--------------------МЕНЮ АДМИНИСТРАТОРА---------------------");
        System.out.println("1. Просмотреть список пользователей");
        System.out.println("2. Редактировать пользователя");
        System.out.println("3. Удалить пользователя");
        System.out.println("4. Добавить фильм");
        System.out.println("5. Удалить фильм");
        System.out.println("6. Выход");
    }

    public void printEditUserMenuView() {
        System.out.println("--------------------МЕНЮ АДМИНИСТРАТОРА---------------------");
        System.out.println("1. Изменить login пользователя");
        System.out.println("2. Изменить password пользователя");
        System.out.println("3. Изменить тип пользователя");
    }

}
