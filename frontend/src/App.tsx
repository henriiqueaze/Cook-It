import { AppRoutes } from "./routes";
import { AuthProvider } from "./contexts/AuthContext";

function App() {
  <AuthProvider>
    <AppRoutes />
  </AuthProvider>;
}

export default App;
