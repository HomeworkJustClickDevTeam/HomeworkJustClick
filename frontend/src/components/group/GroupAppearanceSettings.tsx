import React, {useContext, useEffect, useState} from "react";
import {getGroupPostgresService, changeGroupColorPostgresService} from "../../services/postgresDatabaseServices";
import {AxiosError} from "axios";
import {useParams} from "react-router-dom";
import {colorsArray} from "../../assets/colors";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function GroupAppearanceSettings() {
  const {applicationState, setApplicationState} = useContext(ApplicationStateContext)
  const handleColorChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    try {
      let group = applicationState?.group
      if(group !== undefined){
        group.color = +event.target.value
        setApplicationState({type:"setGroupView", group})
      }
      await changeGroupColorPostgresService(applicationState?.group?.id as unknown as string, +event.target.value).catch((error: AxiosError) => {
        console.log("AXIOS ERROR: ", error)
      })
    } catch (e) {
      console.log(e);
    }
  }
  return (

    <form className='flex inline-block flex-wrap gap-2'>
      {Array.apply(0, Array(20)).map((x, i) => {
        return (
          <div className='flex inline-block flex-wrap w-fit'>
            <label key={i} className='flex inline-block pr-1'>
              <input type="radio" value={i.toString()} name="index" checked={applicationState?.group?.color === i} onChange={handleColorChange}
                     className='w-fit cursor-pointer'/>
              <div
                className={`h-5 w-5 ${colorsArray[i]} border border-solid border-black items-center mt-0.5 ml-1`}></div>
            </label>
          </div>
        )
      })}
    </form>
  )

}