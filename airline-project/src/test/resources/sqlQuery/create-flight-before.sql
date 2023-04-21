INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (1, '17000012', 'Embraer E170STD', 2002, 3800);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (2, '5134', 'Airbus A320-200', 2011, 4300);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (3, '35283', 'Boeing 737-800', 2008, 5765);

INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (1, 'VKO', 'Внуково', 'Москва', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (2, 'VOG', 'Гумрак', 'Волгоград', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (3, 'MQF', 'Магнитогорск', 'Магнитогорск', 'Россия', 'GMT +5');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (4, 'OMS', 'Омск', 'Омск', 'Россия', 'GMT +6');

INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'MSKOMSK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Омск'));
INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (2, 'MSKVLG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DELAYED',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Волгоград'));
INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (10500, 'TEST', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Волгоград'));
