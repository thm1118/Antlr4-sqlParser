select
    last_name,
    case upper(last_name)
        when 'JONES' then 'The name is Jones'
        when 'SMITH' then 'The name is Smith'
        when 'KING' then 'The name is King'
        else 'The name is something else'
    end namecheck
from employee;

select
	rownum line_number,
	case
		when mod(rownum, 15) = 0 then 'fizz buzz'
		when mod(rownum,  3) = 0 then 'fizz'
		when mod(rownum,  5) = 0 then 'buzz'
		else to_char(rownum)
	end case_result,
	decode(mod(rownum, 15), 0, 'fizz buzz',
		decode(mod(rownum, 3), 0, 'fizz',
		decode(mod(rownum, 5), 0, 'buzz', rownum)
		)
	) decode_result
from dual
connect by level <= 100;