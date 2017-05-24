-- card info table
create table card_info
(
	card_no varchar(255) not null primary key,
	expiring_date_of_points datetime not null,
	expiring_points decimal(19,2) not null,
	issued_by varchar(255) not null,
	issued_date_of_points datetime not null,
	mypage_authenticated varchar(255) null,
	withdraw varchar(255) null,
	points decimal(19,2) not null,
	rank int not null,
	reg_status varchar(255) not null,
	token varchar(255) not null,
	token_expired varchar(255) not null,
	constraint UK_scxsf6x02dia8tjkh1x0nafbg unique (token)
)
;

-- owner table
create table owner
(
	address1 varchar(255) null,
	address2 varchar(255) null,
	address3 varchar(255) null,
	birthday varchar(255) null,
	email varchar(255) null,
	name_mei varchar(255) null,
	name_mei_kana varchar(255) null,
	name_sei varchar(255) null,
	name_sei_kana varchar(255) null,
	password varchar(255) null,
	post_no varchar(255) null,
	sex varchar(255) null,
	tel_no varchar(255) null,
	card_no varchar(255) not null primary key,
	constraint FKs2uskw5rb3687hp17mr0ydgxn foreign key (card_no) references card_info (card_no)
)
;