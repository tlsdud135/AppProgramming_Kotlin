use jspdb;
create table s_message(
mid int(11) not null auto_increment,
uid varchar(15) not null,
msg varchar(100) not null,
favcount int(11) default '0',
replycount int(11) default '0',
date datetime not null,
primary key (mid),
key member_FK_idx (uid),
constraint member_FK foreign key (uid) references s_member (uid) on delete cascade on update no action
)engine=InnoDB default charset=utf8;

create table s_reply(
rid int(11) not null auto_increment,
mid int(11) not null,
uid varchar(12) not null,
date datetime not null,
rmsg varchar(50) not null,
primary key (rid),
key message_FK_idx (mid),
constraint message_FK foreign key (mid) references s_message (mid) on delete cascade on update no action
)engine=InnoDB default charset=utf8;

create table s_member(
uid varchar(10) not null,
name varchar(15) not null,
passwd varchar(12) not null,
email varchar(45) default null,
date date not null,
primary key (uid)
)engine=InnoDB default charset=utf8;