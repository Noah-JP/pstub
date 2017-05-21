-- alter table owner drop foreign key FKs2uskw5rb3687hp17mr0ydgxn
-- drop table if exists card_info
-- drop table if exists owner

-- card info table
create table card_info (card_no varchar(255) not null, expiring_date_of_points datetime not null, expiring_points decimal(19,2) not null, issued_by varchar(255) not null, issued_date_of_points datetime not null, mypage_authenticated varchar(255), points decimal(19,2) not null, rank integer not null, reg_status varchar(255) not null, token varchar(255) not null, token_expired varchar(255) not null, primary key (card_no))

-- owner table
create table owner (address1 varchar(255), address2 varchar(255), address3 varchar(255), birthday varchar(255), email varchar(255), name_mei varchar(255), name_mei_kana varchar(255), name_sei varchar(255), name_sei_kana varchar(255), password varchar(255), post_no varchar(255), sex varchar(255), tel_no varchar(255), card_no varchar(255) not null, primary key (card_no))


-- additional const
alter table card_info add constraint UK_scxsf6x02dia8tjkh1x0nafbg unique (token)
alter table owner add constraint FKs2uskw5rb3687hp17mr0ydgxn foreign key (card_no) references card_info (card_no)