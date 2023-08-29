import {createContext, Dispatch, SetStateAction} from "react"

interface HomePageContextInterface {
  homePageIn: boolean
  setHomePageIn: Dispatch<SetStateAction<boolean>>
}


const HomePageContext = createContext<HomePageContextInterface>({homePageIn: true, setHomePageIn: () => {}})

export default HomePageContext
