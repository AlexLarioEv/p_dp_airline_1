insert into roles (id, name)
values (1, 'ROLE_ADMIN');
insert into roles (id, name)
values (2, 'ROLE_MANAGER');
insert into roles (id, name)
values (3, 'ROLE_PASSENGER');

insert into admin (id, email, security_question, answer_question, password)
values (2, 'Test', 'Test', 'admin@mail.ru', 'Admin123@');
insert into airline_manager (id, email, security_question, answer_question, password)
values (3, 'Test', 'Test', 'manager@mail.ru', 'Manager123@');
insert into passengers (id, email, security_question, answer_question, first_name, last_name, middle_name, birth_date, gender, phone_number,
                        serial_number_passport, passport_issuing_country, passport_issuing_date, password)
values (4, 'passenger@mail.ru', 'Test', 'Test', 'Ivan', 'Ivanov', 'Jovanovich', TO_DATE('2003/11/09', 'YYYY/MM/DD'), 'MALE',
        '79222222222', '2222 222222', 'Russia', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Passenger123@');

insert into user_roles (users_id, roles_id)
values (2, 1);
insert into user_roles (users_id, roles_id)
values (3, 2);
insert into user_roles (users_id, roles_id)
values (4, 3)

