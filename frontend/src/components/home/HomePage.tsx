import React, { useEffect, useRef, useState } from "react"
import { Link } from "react-router-dom"
import { FaCaretDown } from "react-icons/fa"
import { selectUserState } from "../../redux/userStateSlice"
import { setGroup } from "../../redux/groupSlice"
import { setHomePageIn } from "../../redux/homePageInSlice"
import { setRole } from "../../redux/roleSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { GroupHomePageDisplayer } from "../group/GroupHomePageDisplayer"
import { useGetUserGroups } from "../customHooks/useGetUserGroups"

const options = [
  { value: "Wszystkie grupy", label: "Wszystkie grupy" },
  { value: "Grupy uczniowskie", label: "Grupy uczniowskie" },
  { value: "Grupy nauczycielskie", label: "Grupy nauczycielskie" }
];

function HomePage() {
  const [selectedOption, setSelectedOption] = useState(options[0]);
  const userState = useAppSelector(selectUserState)
  const dispatch = useAppDispatch()
  const teacherUserGroups = useGetUserGroups(userState?.id, "teacher")
  const studentsUserGroups = useGetUserGroups(userState?.id, "student")
  const allUserGroups = useGetUserGroups(userState?.id, "all")
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const value = event.target.value;
    const option = options.find((opt) => opt.value === value);
    setSelectedOption(option!);
  };

  useEffect(() => {
    dispatch(setGroup(null))
    dispatch(setHomePageIn(true))
    dispatch(setRole(null))
  }, [])


  return (
    <div className='mx-[7.5%]'>
      <div className='flex inline-block my-5 xl:my-8'>
        <Link to="/create/group" className=''>
          <button type="button"
            className=' border-main_blue h-8 rounded-md border-2 px-3 xl:px-4 xl:h-10 xl:rounded-lg text-main_blue text-sm xl:text-lg hover:bg-hover_gray hover:shadow-md active:bg-opacity-60'
          >Stwórz grupę +</button>
        </Link>
        <div className="absolute right-0 mr-[7.5%] text-sm xl:text-lg">
          <select
            className="flex border-2 pl-2 rounded-md xl:rounded-lg w-44 xl:w-56 h-8 xl:h-10 items-center"
            value={selectedOption.value}
            onChange={handleChange}
          >
            {options.map((option) => (
              <option key={option.value}
                value={option.value}
                className="hover:bg-lilly-bg">
                {option.label}
              </option>
            ))}
          </select>
        </div>
      </div>
      {selectedOption.value === "Wszystkie grupy"
        ? <GroupHomePageDisplayer groups={allUserGroups} /> : selectedOption.value === "Grupy nauczycielskie"
          ? <GroupHomePageDisplayer groups={teacherUserGroups} /> : selectedOption.value === "Grupy uczniowskie"
          && <GroupHomePageDisplayer groups={studentsUserGroups} />
      }
    </div>
  )
}

export default HomePage
