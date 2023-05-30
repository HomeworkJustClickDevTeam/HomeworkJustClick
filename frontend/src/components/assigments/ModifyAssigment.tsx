import { useNavigate, useParams } from "react-router-dom"
import { ChangeEvent, useEffect, useState } from "react"
import { Assigment } from "../../types/types"
import common_request from "../../services/default-request-database"
import ReactDatePicker from "react-datepicker"

function ModifyAssigment() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [assigment, setAssigment] = useState<Assigment>({
    title: "",
    completionDatetime: new Date(),
    taskDescription: "",
    visible: false,
    id: 0,
  })

  useEffect(() => {
    common_request
      .get(`/assigment/${id}`)
      .then((r) => setAssigment(r.data))
      .catch((e) => console.log(e))
  }, [])
  const handleChange = (event: ChangeEvent<HTMLInputElement> | Date) => {
    if (event instanceof Date) {
      setAssigment((prevState) => ({
        ...prevState,
        completionDatetime: event,
      }))
    } else {
      const { name, value, type, checked } = event.target
      setAssigment((prevState) => ({
        ...prevState,
        [name]: type === "checkbox" ? checked : value,
      }))
    }
  }
  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    try {
      console.log(assigment)
      common_request.post(
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
          {" "}
          Tytuł
          <input
            name="title"
            type="text"
            onChange={handleChange}
            value={assigment.title}
          />
        </label>
        <label>
          {" "}
          Opis zadania
          <input
            name="taskDescription"
            type="text"
            onChange={handleChange}
            value={assigment.taskDescription}
          />
        </label>
        <ReactDatePicker
          name="completionDatetime"
          selected={assigment.completionDatetime}
          onChange={(date) => handleChange(date as Date)}
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={15}
          dateFormat="yyyy-MM-dd HH:mm"
        />
        <label>
          Visible:
          <input
            name="visible"
            type="checkbox"
            checked={assigment.visible}
            onChange={handleChange}
          />
        </label>
        <button type="submit">Dodaj zadanie domowe</button>
      </form>
    </>
  )
}
export default ModifyAssigment
