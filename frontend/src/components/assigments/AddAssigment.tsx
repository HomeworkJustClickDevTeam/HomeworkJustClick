import { useNavigate, useParams } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import { AssigmentToSend } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import ReactDatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"
import { AddAssigmentFile } from "./file/AddAssigmentFile"

function AddAssigment() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [assigment, setAssigment] = useState<AssigmentToSend>({
    title: "",
    completionDatetime: new Date(),
    taskDescription: "",
    visible: false,
    max_points: 1,
  })
  const [toSend, setToSend] = useState<boolean>(false)
  const [idAssigment, setIdAssigment] = useState<number>()
  const [toNavigate, setToNavigate] = useState<boolean>(false)

  useEffect(() => {
    if (toNavigate) {
      navigate(`/group/${id}/assignments/`)
    }
  }, [toNavigate])
  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: parseInt(value),
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssigment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    try {
      console.log(assigment)
      postgresqlDatabase
        .post(
          `/assignment/withUserAndGroup/${localStorage.getItem("id")}/${id}`,
          assigment
        )
        .then((r) => {
          setIdAssigment(r.data.id)
          setToSend(true)
        })
    } catch (e) {
      console.log(e)
    }
  }

  return (
    <div className='relative flex flex-col mx-24 mt-6 border border-light_gray border-1 rounded-md pt-4 px-4 h-80'>
      <form onSubmit={handleSubmit} className='flex flex-col gap-3'>
        <label className='pr-3'>
          Tytuł:
          <input name="title" type="text" onChange={handleTextChange} placeholder='Nazwa grupy' className='pl-1 ml-2 border-b-2 border-b-light_gray w-64'/>
        </label>
        <label>
          Opis zadania:
          <input
            name="taskDescription"
            type="text"
            onChange={handleTextChange}
            placeholder='Opis zadania' className='pl-1 ml-2 border-b-2 border-b-light_gray w-80'
          />
        </label>
        <label>
          {" "}
          Maksymalne punkty:
          <input
            name="max_points"
            type="number"
            onChange={handleNumberChange}
            min="1"
            max="10"
            className='pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12'
          />
        </label>
        <label className='flex'>
          <p className='w-36'>Data wykonania: </p>
          <ReactDatePicker

            name="completionDatetime"
            selected={assigment.completionDatetime}
            onChange={handleDateChange}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="yyyy-MM-dd HH:mm"
            className='pl-1 ml-2 border-b-2 border-b-light_gray w-36 cursor-pointer'
          />
        </label>
        <label>
          Widoczne:
          <input
            name="visible"
            type="checkbox"
            checked={assigment.visible}
            onChange={handleCheckboxChange}

          />
        </label>
        <button type="submit" className='absolute top-0 right-0 mr-6 mt-4 px-6 py-1 rounded-lg bg-main_blue text-white'>Utwórz zadanie</button>
      </form>
      <p className='mt-4 mb-2'>Dodaj pliki: </p>
      <AddAssigmentFile
        toSend={toSend}
        idAssigment={idAssigment}
        setToNavigate={setToNavigate}
      />
    </div>
  )
}
export default AddAssigment
