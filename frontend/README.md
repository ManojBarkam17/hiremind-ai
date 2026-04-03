# HireMind AI - Frontend

A modern, enterprise-grade React + TypeScript frontend for the HireMind AI Resume Matching & ATS Optimization Platform.

## Features

- **Resume Analysis**: Upload resumes and match them against job descriptions
- **Real-time Scoring**: Get detailed breakdown of match scores across 6 dimensions
- **AI-Powered Optimization**: Generate AI-optimized resume versions
- **Skills Matching**: See matched and missing skills with recommendations
- **ATS Detection**: Identify and fix ATS compliance issues
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Dark Mode**: Built-in dark theme support
- **Authentication**: Secure JWT-based authentication

## Tech Stack

- **React 18** with TypeScript
- **Vite** for fast development and building
- **Tailwind CSS** for styling
- **React Router** for navigation
- **Axios** for API calls
- **Recharts** for data visualization
- **Lucide Icons** for beautiful icons

## Setup

### Prerequisites

- Node.js 18+ and npm/yarn
- Backend API running on `http://localhost:5000`

### Installation

1. Install dependencies:

```bash
npm install
```

2. Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

3. Update the API URL in `.env` if needed:

```
VITE_API_URL=http://localhost:5000/api
```

## Development

Start the development server:

```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Build

Build for production:

```bash
npm run build
```

The output will be in the `dist` directory.

## Project Structure

```
src/
├── api/                 # API client and endpoints
│   ├── axiosConfig.ts  # Axios configuration with JWT interceptor
│   ├── authApi.ts      # Authentication endpoints
│   ├── resumeApi.ts    # Resume management endpoints
│   ├── analysisApi.ts  # Analysis endpoints
│   └── optimizationApi.ts
├── components/         # Reusable React components
│   ├── layout/        # Layout components (Navbar, Sidebar, ProtectedRoute)
│   ├── common/        # Common components (Button, Card, Badge, Modal, etc.)
│   ├── dashboard/     # Dashboard components (StatsCard, RecentAnalyses)
│   ├── upload/        # File upload component
│   ├── analysis/      # Analysis components (ScoreCard, SkillsPanel, ATSIssues, etc.)
│   └── optimization/  # Optimization components (BeforeAfterViewer, ChangeLog, ExportButton)
├── context/           # React context (AuthContext)
├── hooks/             # Custom hooks (useAuth, useAnalysis)
├── pages/             # Page components
│   ├── LoginPage.tsx
│   ├── DashboardPage.tsx
│   ├── UploadResumePage.tsx
│   ├── PasteJDPage.tsx
│   ├── AnalysisResultsPage.tsx
│   ├── OptimizationPage.tsx
│   ├── ComparisonPage.tsx
│   └── HistoryPage.tsx
├── types/             # TypeScript type definitions
├── App.tsx            # Main app component with routing
├── main.tsx           # Entry point
└── index.css          # Global styles
```

## Key Components

### ScoreCard
Displays a circular progress indicator showing overall match score with color-coded feedback.

### ScoreBreakdown
Radar chart showing 6 dimensions of scoring:
- Skills Match
- Experience Relevance
- Education Alignment
- Keyword Optimization
- Formatting Compliance
- Achievements

### SkillsPanel
Side-by-side display of matched skills (green badges) and missing skills (red badges) with proficiency levels.

### ATSIssues
Expandable issue list with severity levels (critical, warning, info) and actionable suggestions.

### FileUpload
Drag-and-drop file upload component with validation for PDF/DOCX files up to 10MB.

### BeforeAfterViewer
Side-by-side comparison of original and optimized resume content.

### ChangeLog
Timeline view of all changes made to the resume with categorization (added, modified, removed).

## Authentication Flow

1. User logs in or creates account on LoginPage
2. JWT token is stored in localStorage
3. JWT token is automatically included in all API requests via axios interceptor
4. If token is invalid or expired, user is redirected to login
5. ProtectedRoute component prevents unauthorized access

## Styling

The application uses Tailwind CSS with custom enterprise theme:

- **Primary Color**: Indigo (#4f46e5)
- **Secondary Color**: Blue (#3b82f6)
- **Success**: Green (#10b981)
- **Warning**: Amber/Yellow (#f59e0b)
- **Danger**: Red (#ef4444)

Dark mode is supported via Tailwind's `dark:` prefix.

## API Integration

The frontend uses axios with a custom configuration that:
- Sets base URL from `VITE_API_URL` environment variable
- Automatically attaches JWT token to all requests
- Handles 401 responses by redirecting to login
- Intercepts errors and provides user feedback

## Performance Considerations

- Lazy loading of pages via React Router
- Optimized re-renders using memoization where needed
- Efficient state management with React Context
- Tailwind CSS purging unused styles in production

## Browser Support

- Chrome/Edge (latest 2 versions)
- Firefox (latest 2 versions)
- Safari (latest 2 versions)

## Contributing

1. Follow the existing code structure
2. Use TypeScript for type safety
3. Use Tailwind CSS for styling
4. Keep components small and reusable
5. Add proper error handling

## License

Proprietary - HireMind AI
