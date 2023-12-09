alter table users
add constraint chk_country_name check(length(trim(country)) >= 2);