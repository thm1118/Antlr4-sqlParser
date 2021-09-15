--New PIVOT syntax.
--Pivoted launch success and failure per year.
select *
from
(
	--Orbital and deep space launches.
	select to_char(launch_date, 'YYYY') launch_year, launch_status
	from launch
	where launch_category in ('orbital', 'deep space')
) launches
pivot
(
	count(*)
	for launch_status in
	(
		'success' as success,
		'failure' as failure
	)
)
order by launch_year;


--UNPIVOT example.
--Unpivot data with UNPIVOT syntax.
select *
from
(
	select launch_id, flight_id1, flight_id2
	from launch
	where launch_category in ('orbital', 'deep space')
) launches
unpivot
(
	flight_name for
	flight_id in (flight_id1 as 1, flight_id2 as 2)
)
order by launch_id;