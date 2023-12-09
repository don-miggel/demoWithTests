alter table users
add constraint chk_gnd_correct_values check ( gender in ('M','F') );