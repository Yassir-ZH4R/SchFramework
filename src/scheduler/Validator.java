package scheduler;

public class Validator {

    /**
     * Simple validation function.
     * Make sure the correct resource file was provided in the schedule.draw() call
     * if the problem is ressource constrained.
     * @param args File name of the schedule to check
     */
	public static void main(String[] args) {

		Schedule schedule = null;
		String problemName = null;
		String resourceName = null;
        RC rc = null;
        Graph g = null;
        // simple check
		if (args.length == 1){
			System.out.println("Reading schedule from "+args[0]+"\n");
            // parsing schedule file
			ScheduleReader reader = new ScheduleReader();
			schedule = reader.parse(args[0]);
			problemName = reader.getProblemName();
			resourceName = reader.getResourceName();
            // graph file provided as well
            if (problemName != null) {
                ProblemReader dr = new ProblemReader(false);
                g = dr.parse("graphs/" + problemName);
            }
            // resource file provided as well
            if (resourceName != null) {
                rc = new RC();
                rc.parse(resourceName);
            }
		}
        else {
            System.out.println("Incorrect number of arguments! This function requires one argument as input.\n");
            System.out.println("Please provide the path to the schedule to validate.\n");
            return;
        }

		System.out.println("Problem name: " + problemName);
		System.out.println("Resource file: " + resourceName);
		System.out.printf("\n" + schedule.diagnose());

		//schedule.draw("schedules/Validator.dot", problemName, resourceName);

		/* validate the schedule */

		Node conflictingNode = schedule.validateDependencies();
		if (conflictingNode != null)
			System.out.println("Schedule validation failed. First conflicting node: " + conflictingNode.id);

		Node overusingNode = schedule.validateResources(rc);
		if (overusingNode != null)
			System.out.println("Resource usage validation failed. First overuse by node " + overusingNode.id);

        Node graphViolatingNode = schedule.validateNodes(g);
        if (graphViolatingNode != null)
            System.out.println("Schedule does not match the graph. First conflicting node " + graphViolatingNode.id);

	}
}
