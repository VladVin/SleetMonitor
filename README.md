## Sleet Monitor
Sleet Monitor - система детектирования падений человека на скользкой поверхности.

## Как это работает?
Со множества пользователей, имеющих мобильное приложение Sleet Monitor, собираются данные в реальном времени (показания акселерометра и местоположение) и отправляются в облако Windows Azure. В облаке происходит следующая обработка этих данных - имеется заранее натренированная с помощью алгоритма машинного обучения модель, способная предсказывать по новым данным, свойственны такие показания акселерометра падению человека на льду или нет. Зная информацию о падениях людей, проходящих по той или иной улице в городе, можно поставить оценку этой улице по балловой шкале гололедицы. Люди, находясь дома и планируя выходить куда-то, запускают приложение и выбирают наиболее безопасный маршрут для перемешения из пункта A в пункт B.