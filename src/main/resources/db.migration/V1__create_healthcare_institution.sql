create table healthcare_institution(
                                       id bigserial not null primary key,
                                       name_institution varchar(150) not null,
                                       cnpj varchar(14) not null,
                                       pixeon_coins integer default 20 not null
);