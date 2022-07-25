create table exam(
                     id bigserial not null primary key,
                     patient_name varchar(100) not null,
                     patient_age integer not null,
                     patient_gender varchar(10) check(patient_gender in ('FEMALE', 'MALE')) not null,
                     physician_name varchar(100) not null,
                     physician_crm varchar(20) not null,
                     procedure_name varchar(200) not null,
                     first_request boolean default true not null,
                     id_institution bigint references healthcare_institution(id) not null,
                     data_cadastro timestamp default current_timestamp not null
);