---------------------------------------------------------------------------
-- Example
---------------------------------------------------------------------------

--Top 3 fuels used per year using ANSI join syntax.
--
--#6: Select only the top N.
select launch_year, fuel, launch_count
from
(
	--#5: Rank the fuel counts.
	select launch_year, launch_count, fuel,
		row_number() over
			(partition by launch_year order by launch_count desc) rownumber
	from
	(
		--#4: Count of fuel used per year.
		select
			to_char(launches.launch_date, 'YYYY') launch_year,
			count(*) launch_count,
			engine_fuels.fuel
		from
		(
			--#1: Orbital and deep space launches.
			select *
			from launch
			where launch_category in ('orbital', 'deep space')
		) launches
		left join
		(
			--#2: Launch Vehicle Engines
			select launch_vehicle_stage.lv_id, stage.engine_id
			from launch_vehicle_stage
			left join stage
				on launch_vehicle_stage.stage_name = stage.stage_name
		) lv_engines
			on launches.lv_id = lv_engines.lv_id
		left join
		(
			--#3: Engine Fuels
			select engine.engine_id, propellant_name fuel
			from engine
			left join engine_propellant
				on engine.engine_id = engine_propellant.engine_id
			left join propellant
				on engine_propellant.propellant_id = propellant.propellant_id
			where oxidizer_or_fuel = 'fuel'
		) engine_fuels
			on lv_engines.engine_id = engine_fuels.engine_id
		group by to_char(launch_date, 'YYYY'), fuel
		order by launch_year, launch_count desc, fuel
	)
)
where rownumber <= 3
order by launch_year, launch_count desc