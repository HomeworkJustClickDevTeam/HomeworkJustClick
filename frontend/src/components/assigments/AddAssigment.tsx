import { useNavigate, useParams } from "react-router-dom"
import React, { ChangeEvent, useState } from "react"
import { AssigmentToSend } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import ReactDatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"

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
      postgresqlDatabase.post(
        `/assignment/withUserAndGroup/${localStorage.getItem("id")}/${id}`,
        assigment
      )
      navigate(`/group/${id}/assignments/`)
    } catch (e) {
      console.log(e)
    }
  }

  return (
    <>
      <form onSubmit={handleSubmit}>
        <label>
          Tytuł:
          <input name="title" type="text" onChange={handleTextChange} />
        </label>
        <label>
          Opis zadania:
          <input
            name="taskDescription"
            type="text"
            onChange={handleTextChange}
          />
        </label>
        <label>
          {" "}
          Punkty
          <input
            name="max_points"
            type="number"
            onChange={handleNumberChange}
            min="1"
            max="10"
          />
        </label>
        <label>
          Data zrobienia
          <ReactDatePicker
            name="completionDatetime"
            selected={assigment.completionDatetime}
            onChange={handleDateChange}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="yyyy-MM-dd HH:mm"
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
        <button type="submit">Dodaj zadanie domowe</button>
      </form>
    </>
  )
}
export default AddAssigment
