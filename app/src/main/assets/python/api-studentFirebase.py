from flask import Flask, request, jsonify
from flask_cors import CORS  # Import CORS
import firebase_admin
from firebase_admin import credentials, firestore, auth
from google.api_core.exceptions import NotFound

app = Flask(__name__)
CORS(app, supports_credentials=True)  # Enable CORS and allow credentials

# Initialize Firebase
cred = credentials.Certificate('../../eval360-ff5bd-firebase-adminsdk-nmg6x-4b548fdf51.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

@app.route('/student-details/<email>', methods=['GET'])
def get_student_details(email):
    try:
        students_ref = db.collection('students')
        student_query = students_ref.where('email', '==', email).limit(1)
        student = student_query.get()
        if student:
            return jsonify(student.to_dict()), 200
        else:
            return jsonify({"error": "Student not found"}), 404
    except Exception as e:
        return f"An Error Occurred: {e}"

@app.route('/student-projects/<student_id>', methods=['GET'])
def get_projects_by_student(student_id):
    student_ref = db.collection('students').document(student_id)
    student = student_ref.get()

    if not student.exists:
        return jsonify({'error': 'Student not found'}), 404

    student_data = student.to_dict()
    project_ids = student_data.get('projects', [])
    
    projects = []
    for project_id in project_ids:
        project_ref = db.collection('projects').document(project_id)
        project = project_ref.get()
        if project.exists:
            project_data = project.to_dict()
            group_ids = project_data.get('groups', [])
            
            group_details = []
            for group_id in group_ids:
                group_ref = db.collection('groups').document(group_id)
                group = group_ref.get()
                if group.exists:
                    group_data = group.to_dict()
                    students_ids = group_data.get('students', [])
                    students_details = [db.collection('students').document(s_id).get().to_dict() for s_id in students_ids]
                    group_details.append({'groupId': group_id, 'groupName': group_data.get('groupName', ''), 'students': students_details})

            projects.append({'id': project_id, 'projectName': project_data.get('projectName', ''), 'groups': group_details})

    return jsonify(projects)


@app.route('/project-details/<project_id>', methods=['GET'])
def get_project_details(project_id):
    project_ref = db.collection('projects').document(project_id)
    project = project_ref.get()

    if not project.exists:
        return jsonify({'error': 'Project not found'}), 404

    project_data = project.to_dict()
    group_ids = project_data.get('groups', [])

    groups = []
    for group_id in group_ids:
        group_ref = db.collection('groups').document(group_id)
        group = group_ref.get()
        if group.exists:
            group_data = group.to_dict()
            group_students = [db.collection('students').document(student_id).get().to_dict() for student_id in group_data.get('students', [])]
            groups.append({'id': group_id, 'groupName': group_data.get('groupName', ''), 'students': group_students})

    project_details = {'id': project_id, 'projectName': project_data.get('projectName', ''), 'groups': groups}
    
    return jsonify(project_details)

@app.route('/student/<student_id>', methods=['GET'])
def get_student(student_id):
    student_ref = db.collection('students').document(student_id)
    student = student_ref.get()
    if student.exists:
        student_data = student.to_dict()
        student_data['id'] = student_id  # Add id to student data
        return jsonify(student_data), 200
    else:
        return jsonify({"error": "Student not found"}), 404


@app.route('/group/<group_id>', methods=['GET'])
def get_group(group_id):
    group_ref = db.collection('groups').document(group_id)
    group = group_ref.get()
    if group.exists:
        group_data = group.to_dict()
        group_students = [db.collection('students').document(student_id).get().to_dict() for student_id in group_data.get('students', [])]
        group_data['students'] = group_students
        return jsonify(group_data), 200
    else:
        return jsonify({"error": "Group not found"}), 404

@app.route('/grade-student', methods=['POST'])
def grade_student():
    data = request.get_json()
    student_id = data.get('studentId')
    evaluator_id = data.get('evaluatorId')
    grade = data.get('grade')

    # TODO: validate input here

    # store the grade in your database
    db.collection('students').document(student_id).update({
        f'grades.{evaluator_id}': grade
    })

    return jsonify({"message": "Grade submitted successfully"}), 200



if __name__ == '__main__':
    app.run(port=5002, debug=True)