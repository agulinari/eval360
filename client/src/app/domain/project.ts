import { Reviewer } from "./reviewer";
import { FeedbackProvider } from "./feedback-provider";
import { Evaluee } from "./evaluee";
import { ProjectAdmin } from "./project-admin";

export class Project {
    id: number;
    status: string;
    name: string;
    description: string;
    reviewers: Reviewer[];
    feedbackProviders: FeedbackProvider[];
    evaluees: Evaluee[];
    projectAdmins: ProjectAdmin[];
}
