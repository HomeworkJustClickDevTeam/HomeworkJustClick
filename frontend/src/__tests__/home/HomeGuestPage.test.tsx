import HomeGuestPage from "../../components/home/HomeGuestPage";
import renderer from "react-test-renderer";
import { Provider } from "react-redux";
import { configureStore } from "@reduxjs/toolkit";
import { setupStore } from "../../redux/store";
import { BrowserRouter } from "react-router-dom";


it("checks if home guest page renders correctly", () => {
  const renderedHomeGuestPage = renderer
    .create(
      <Provider store={setupStore()}>
        <BrowserRouter>
          <HomeGuestPage></HomeGuestPage>
        </BrowserRouter>
      </Provider>
    )
  expect(renderedHomeGuestPage).toMatchSnapshot()
});
